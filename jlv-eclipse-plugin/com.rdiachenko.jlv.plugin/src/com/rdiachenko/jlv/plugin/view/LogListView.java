package com.rdiachenko.jlv.plugin.view;

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
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.part.ViewPart;

import com.rdiachenko.jlv.plugin.JlvConstants;
import com.rdiachenko.jlv.plugin.LogField;

public class LogListView extends ViewPart {

    private final QuickLogFilter quickFilter = new QuickLogFilter();

    private IContextActivation context;
    private TableViewer viewer;
    private Text quickSearchField;

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
        } finally {
            super.dispose();
        }
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
    
    private TableViewer createViewer(Composite parent) {
        int style = SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION;
        TableViewer viewer = new TableViewer(parent, style);
        viewer.setUseHashlookup(true);
//        viewer.setContentProvider(null);
//        viewer.setInput(null);
        viewer.addFilter(quickFilter);

        for (LogField field : LogField.values()) {
            TableViewerColumn columnViewer = new TableViewerColumn(viewer, SWT.NONE);
            columnViewer.getColumn().setText(field.getName());
            columnViewer.getColumn().setResizable(true);
            columnViewer.getColumn().setMoveable(false);
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
