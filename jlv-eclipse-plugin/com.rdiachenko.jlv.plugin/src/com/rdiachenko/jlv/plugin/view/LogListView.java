package com.rdiachenko.jlv.plugin.view;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rdiachenko.jlv.Log;
import com.rdiachenko.jlv.plugin.JlvConstants;
import com.rdiachenko.jlv.plugin.LogField;
import com.rdiachenko.jlv.plugin.QuickLogFilter;

public class LogListView extends ViewPart {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String SASH_LEFT_WIDTH_KEY = "LogListView.SASH_LEFT_WIDTH";
    private static final String SASH_RIGHT_WIDTH_KEY = "LogListView.SASH_RIGHT_WIDTH";

    private QuickLogFilter quickFilter;
    private LogListViewController controller;
    private IContextActivation context;
    private IMemento memento;

    private SashForm sash;
    private TableViewer logListViewer;
    private LogDetailsViewer logDetailsViewer;

    private Text quickSearchField;
    private boolean scrollToBottom;

    @Override
    public void init(IViewSite site, IMemento memento) throws PartInitException {
        super.init(site, memento);
        this.memento = memento;
        quickFilter = new QuickLogFilter();
        controller = new LogListViewController(this);
    }

    @Override
    public void createPartControl(Composite parent) {
        IContextService contextService = getSite().getService(IContextService.class);

        if (contextService != null) {
            context = contextService.activateContext(JlvConstants.LOGLIST_CONTEXT_ID);
        }

        GridLayout parentLayout = new GridLayout();
        parentLayout.verticalSpacing = 0;
        parentLayout.marginWidth = 0;
        parentLayout.marginHeight = 0;
        parent.setLayout(parentLayout);

        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 2;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        sash = new SashForm(composite, SWT.HORIZONTAL);
        sash.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        logListViewer = createLogListViewer(sash);
        logDetailsViewer = new LogDetailsViewer(sash);
        int leftWidth = 60;
        int rightWidth = 40;

        if (memento != null && memento.getInteger(SASH_LEFT_WIDTH_KEY) != null
                && memento.getInteger(SASH_RIGHT_WIDTH_KEY) != null) {
            leftWidth = memento.getInteger(SASH_LEFT_WIDTH_KEY);
            rightWidth = memento.getInteger(SASH_RIGHT_WIDTH_KEY);
        }
        sash.setWeights(new int[] { leftWidth, rightWidth });

        quickSearchField = createQuickSearchField(parent);
        controller.startViewRefresher();
    }

    @Override
    public void setFocus() {
        logListViewer.getControl().setFocus();
    }

    @Override
    public void saveState(IMemento memento) {
        super.saveState(memento);
        int[] weights = sash.getWeights();
        memento.putInteger(SASH_LEFT_WIDTH_KEY, weights[0]);
        memento.putInteger(SASH_RIGHT_WIDTH_KEY, weights[1]);
        logger.info("LogListView state saved");
    }

    @Override
    public void dispose() {
        try {
            IContextService contextService = getSite().getService(IContextService.class);

            if (contextService != null) {
                contextService.deactivateContext(context);
            }
            controller.dispose();
        } finally {
            super.dispose();
        }
    }

    public LogListViewController getController() {
        return controller;
    }

    public void setSearchFieldVisible(boolean visible) {
        GridData gridData = (GridData) quickSearchField.getLayoutData();
        gridData.exclude = !visible;
        quickSearchField.setVisible(visible);
        quickSearchField.getParent().layout();

        if (visible) {
            quickSearchField.selectAll();
            quickSearchField.setFocus();
        } else {
            setFocus();
        }
    }

    public boolean isSearchFieldVisible() {
        return quickSearchField.isVisible();
    }

    public void toggleScrollToBottom() {
        scrollToBottom = !scrollToBottom;
    }

    public void clear() {
        controller.getInput().clear();
        logListViewer.getTable().removeAll();
        logDetailsViewer.clear();
    }

    public void refresh() {
        if (!logListViewer.getTable().isDisposed()) {
            if (scrollToBottom) {
                Table table = logListViewer.getTable();
                int itemCount = table.getItemCount();
                table.setSelection(itemCount - 1);
            }
            logListViewer.refresh(true, scrollToBottom);
        }
    }

    private TableViewer createLogListViewer(Composite parent) {
        int style = SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION;
        TableViewer viewer = new TableViewer(parent, style);
        viewer.setUseHashlookup(true);
        viewer.setContentProvider(new LogListContentProvider());
        viewer.setInput(controller.getInput());
        viewer.addFilter(quickFilter);
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                StructuredSelection selection = (StructuredSelection) event.getSelection();
                Log log = (Log) selection.getFirstElement();

                if (log != null) {
                    logDetailsViewer.showDetails(log);
                }
            }
        });

        for (LogField field : LogField.values()) {
            TableViewerColumn columnViewer = new TableViewerColumn(viewer, SWT.NONE);
            columnViewer.getColumn().setText(field.getName());
            columnViewer.getColumn().setWidth(100);
            columnViewer.getColumn().setResizable(true);
            columnViewer.getColumn().setMoveable(false);

            switch (field) {
            case MESSAGE:
            case THROWABLE:
            case MDC:
            case NDC:
                columnViewer.setLabelProvider(new LimitedTextColumnLabelProvider(field));
                break;
            default:
                columnViewer.setLabelProvider(new DefaultColumnLabelProvider(field));
            }
        }
        Table table = viewer.getTable();
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        return viewer;
    }

    private Text createQuickSearchField(Composite parent) {
        Text searchField = new Text(parent, SWT.BORDER);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        searchField.setLayoutData(gridData);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent key) {
                quickFilter.setSearchText(searchField.getText());
            }
        });
        return searchField;
    }
}
