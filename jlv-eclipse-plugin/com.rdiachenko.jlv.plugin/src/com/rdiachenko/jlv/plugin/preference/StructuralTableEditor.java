package com.rdiachenko.jlv.plugin.preference;

import java.util.Collections;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.OwnerDrawLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.rdiachenko.jlv.plugin.ImageResource;
import com.rdiachenko.jlv.plugin.PreferenceStoreUtils;
import com.rdiachenko.jlv.plugin.ResourceManager;

public class StructuralTableEditor extends FieldEditor {
    
    private static final String DISPLAY_COLUMN_HEADER = "Display";
    private static final String NAME_COLUMN_HEADER = "Name";
    private static final String[] COLUMN_NAMES = {
            DISPLAY_COLUMN_HEADER,
            NAME_COLUMN_HEADER
    };

    private static final int DISPLAY_COLUMN_WIDTH = 80;
    private static final int NAME_COLUMN_WIDTH = 160;
    private static final int[] COLUMN_WIDTHS = { DISPLAY_COLUMN_WIDTH, NAME_COLUMN_WIDTH };
    
    private StructuralModel model;

    private Composite topComposite;
    
    private TableViewer tableViewer;
    private Button upButton;
    private Button downButton;

    public StructuralTableEditor(Composite parent) {
        createControl(parent);
    }
    
    @Override
    protected void adjustForNumColumns(int numColumns) {
        ((GridData) topComposite.getLayoutData()).horizontalSpan = numColumns;
    }
    
    @Override
    protected void doFillIntoGrid(Composite parent, int numColumns) {
        topComposite = parent;
        tableViewer = createTableViewerControl(parent);

        GridLayout buttonBoxLayout = new GridLayout();
        buttonBoxLayout.marginWidth = 0;
        buttonBoxLayout.marginHeight = 0;
        Composite buttonBox = new Composite(parent, SWT.NULL);
        buttonBox.setLayout(buttonBoxLayout);

        GridData layoutData = new GridData();
        layoutData.verticalAlignment = GridData.BEGINNING;
        buttonBox.setLayoutData(layoutData);

        upButton = createPushButton(buttonBox, "Up", new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                upPressed();
            }
        });
        downButton = createPushButton(buttonBox, "Down", new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                downPressed();
            }
        });
        selectionChanged();
    }
    
    @Override
    protected void doLoad() {
        model = PreferenceStoreUtils.getStructuralModel();
        updateUiState();
    }
    
    @Override
    protected void doLoadDefault() {
        model = PreferenceStoreUtils.getDefaultStructuralModel();
        updateUiState();
    }
    
    @Override
    protected void doStore() {
        PreferenceStoreUtils.setStructuralModel(model);
    }
    
    @Override
    public int getNumberOfControls() {
        return 2; // Table composite and Button box composite
    }

    private void updateUiState() {
        tableViewer.setInput(model);
        tableViewer.refresh();
    }
    
    private TableViewer createTableViewerControl(Composite parent) {
        TableViewer viewer = new TableViewer(parent, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION
                | SWT.HIDE_SELECTION);
        viewer.setUseHashlookup(true);
        viewer.setContentProvider(new ModelContentProvider());
        
        for (int i = 0; i < COLUMN_NAMES.length; i++) {
            TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.LEAD);
            viewerColumn.getColumn().setText(COLUMN_NAMES[i]);
            viewerColumn.getColumn().setWidth(COLUMN_WIDTHS[i]);

            switch (COLUMN_NAMES[i]) {
            case DISPLAY_COLUMN_HEADER:
                viewerColumn.setLabelProvider(new DisplayColumnLabelProvider());
                viewerColumn.setEditingSupport(new DisplayCellEditor(viewer));
                break;
            case NAME_COLUMN_HEADER:
                viewerColumn.setLabelProvider(new ColumnLabelProvider() {
                    @Override
                    public String getText(Object element) {
                        StructuralModelItem item = (StructuralModelItem) element;
                        return item.getFieldName();
                    }
                });
                break;
            default:
                throw new IllegalArgumentException("No column with such a name: " + COLUMN_NAMES[i]
                        + ". Only [Name, Width, Display] are allowed.");
            }
        }
        Table table = viewer.getTable();
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        table.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                selectionChanged();
            }
        });
        return viewer;
    }
    
    private void selectionChanged() {
        int index = tableViewer.getTable().getSelectionIndex();
        int size = tableViewer.getTable().getItemCount();

        upButton.setEnabled(size > 1 && index > 0);
        downButton.setEnabled(size > 1 && index >= 0 && index < size - 1);
    }

    private Button createPushButton(Composite parent, String name, SelectionListener selectionListener) {
        Button button = new Button(parent, SWT.PUSH);
        button.setText(name);
        button.addSelectionListener(selectionListener);
        GridData gridData = new GridData(SWT.NONE);
        gridData.widthHint = 95;
        button.setLayoutData(gridData);
        return button;
    }

    private void upPressed() {
        swap(true);
    }
    
    private void downPressed() {
        swap(false);
    }
    
    private void swap(boolean up) {
        int index = tableViewer.getTable().getSelectionIndex();
        int target = up ? index - 1 : index + 1;
        
        if (index >= 0) {
            Collections.swap(model.getModelItems(), index, target);
            tableViewer.refresh();
            tableViewer.getTable().setSelection(target);
        }
        selectionChanged();
    }

    private static class ModelContentProvider implements IStructuredContentProvider {
        
        @Override
        public void dispose() {
            // no code
        }
        
        @Override
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            // no code
        }
        
        @Override
        public Object[] getElements(Object inputElement) {
            StructuralModel model = (StructuralModel) inputElement;
            return model.getModelItems().toArray();
        }
    }

    private static class DisplayColumnLabelProvider extends OwnerDrawLabelProvider {
        
        @Override
        protected void measure(Event event, Object element) {
            // no code
        }
        
        @Override
        protected void paint(Event event, Object element) {
            TableItem tableItem = (TableItem) event.item;
            StructuralModelItem modelItem = (StructuralModelItem) tableItem.getData();
            Image image = modelItem.isDisplay() ? ResourceManager.getImage(ImageResource.CHECKBOX_CHECKED_ICON)
                    : ResourceManager.getImage(ImageResource.CHECKBOX_UNCHECKED_ICON);
            Rectangle bounds = tableItem.getBounds(event.index);
            Rectangle imageBounds = image.getBounds();
            int xOffset = bounds.width / 2 - imageBounds.width / 2;
            int yOffset = bounds.height / 2 - imageBounds.height / 2;
            int x = xOffset > 0 ? event.x + xOffset : event.x;
            int y = yOffset > 0 ? event.y + yOffset : event.y;
            event.gc.drawImage(image, x, y);
        }
    }

    private static class DisplayCellEditor extends EditingSupport {
        
        private final TableViewer viewer;
        
        private final CellEditor editor;
        
        public DisplayCellEditor(TableViewer viewer) {
            super(viewer);
            this.viewer = viewer;
            editor = new CheckboxCellEditor(viewer.getTable(), SWT.CHECK | SWT.CENTER);
        }
        
        @Override
        protected CellEditor getCellEditor(final Object element) {
            return editor;
        }
        
        @Override
        protected boolean canEdit(final Object element) {
            return true;
        }
        
        @Override
        protected Object getValue(final Object element) {
            return ((StructuralModelItem) element).isDisplay();
        }
        
        @Override
        protected void setValue(final Object element, final Object value) {
            ((StructuralModelItem) element).setDisplay((Boolean) value);
            viewer.update(element, null);
        }
    }
}
