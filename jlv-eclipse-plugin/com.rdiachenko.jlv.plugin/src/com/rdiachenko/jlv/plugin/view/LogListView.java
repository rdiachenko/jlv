package com.rdiachenko.jlv.plugin.view;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
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
    
    private QuickLogFilter quickFilter;
    private LogListViewController controller;
    private IContextActivation context;
    private TableViewer viewer;
    private Text quickSearchField;
    private boolean scrollToBottom;
    
    @Override
    public void init(IViewSite site) throws PartInitException {
        super.init(site);
        quickFilter = new QuickLogFilter();
        controller = new LogListViewController(this);
    }

    @Override
    public void createPartControl(Composite parent) {
        IContextService contextService = getSite().getService(IContextService.class);

        if (contextService != null) {
            context = contextService.activateContext(JlvConstants.LOGLIST_CONTEXT_ID);
        }
        
        GridLayout layout = new GridLayout();
        layout.verticalSpacing = 0;
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        parent.setLayout(layout);
        
        viewer = createViewer(parent);
        quickSearchField = createQuickSearchField(parent);
        controller.startViewRefresher();
    }
    
    @Override
    public void setFocus() {
        viewer.getControl().setFocus();
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
        viewer.getTable().removeAll();
    }

    public void refresh() {
        if (!viewer.getTable().isDisposed()) {
            if (scrollToBottom) {
                Table table = viewer.getTable();
                int itemCount = table.getItemCount();
                table.setSelection(itemCount - 1);
            }
            viewer.refresh(true, scrollToBottom);
        }
    }

    private TableViewer createViewer(Composite parent) {
        int style = SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION;
        TableViewer viewer = new TableViewer(parent, style);
        viewer.setUseHashlookup(true);
        viewer.setContentProvider(new LogListContentProvider());
        viewer.setInput(controller.getInput());
        viewer.addFilter(quickFilter);
        
        for (LogField field : LogField.values()) {
            TableViewerColumn columnViewer = new TableViewerColumn(viewer, SWT.NONE);
            columnViewer.getColumn().setText(field.getName());
            columnViewer.getColumn().setWidth(100);
            columnViewer.getColumn().setResizable(true);
            columnViewer.getColumn().setMoveable(false);
            columnViewer.setLabelProvider(new ColumnLabelProvider() {
                @Override
                public String getText(Object element) {
                    Log log = (Log) element;
                    return log.getLevel();
                }
            });
        }
        
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        Table table = viewer.getTable();
        table.setLayoutData(gridData);
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
