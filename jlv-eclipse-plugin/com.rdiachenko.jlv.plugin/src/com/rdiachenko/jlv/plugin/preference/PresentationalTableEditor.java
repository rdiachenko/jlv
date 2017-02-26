package com.rdiachenko.jlv.plugin.preference;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColorCellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.OwnerDrawLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.rdiachenko.jlv.plugin.LogLevel;
import com.rdiachenko.jlv.plugin.PreferenceStoreUtils;
import com.rdiachenko.jlv.plugin.ResourceManager;

public class PresentationalTableEditor extends FieldEditor {
    
    private static final String LEVEL_COLUMN_HEADER = "Level";
    private static final String FOREGROUND_COLUMN_HEADER = "Foreground";
    private static final String BACKGROUND_COLUMN_HEADER = "Background";
    private static final String[] COLUMN_NAMES = {
            LEVEL_COLUMN_HEADER,
            FOREGROUND_COLUMN_HEADER,
            BACKGROUND_COLUMN_HEADER
    };
    
    private static final int LEVEL_COLUMN_WIDTH = 60;
    private static final int FOREGROUND_COLUMN_WIDTH = 300;
    private static final int BACKGROUND_COLUMN_WIDTH = 300;
    private static final int[] COLUMN_WIDTHS = { LEVEL_COLUMN_WIDTH, FOREGROUND_COLUMN_WIDTH, BACKGROUND_COLUMN_WIDTH };
    
    private PresentationalModel model;
    
    private Composite topComposite;
    
    private Button imageSwitcherControl;
    private Spinner spinnerControl;
    private TableViewer tableViewer;
    
    public PresentationalTableEditor(Composite parent) {
        createControl(parent);
    }
    
    @Override
    protected void adjustForNumColumns(int numColumns) {
        ((GridData) topComposite.getLayoutData()).horizontalSpan = numColumns;
    }
    
    @Override
    protected void doFillIntoGrid(Composite parent, int numColumns) {
        topComposite = parent;
        imageSwitcherControl = createImageSwitcherControl(parent);
        spinnerControl = createSpinnerBoxControl(parent);
        tableViewer = createTableViewerControl(parent);
    }
    
    @Override
    protected void doLoad() {
        model = PreferenceStoreUtils.getPresentationalModel();
        updateUiState();
    }
    
    @Override
    protected void doLoadDefault() {
        model = PreferenceStoreUtils.getDefaultPresentationalModel();
        updateUiState();
    }
    
    @Override
    protected void doStore() {
        PreferenceStoreUtils.setPresentationalModel(model);
    }
    
    @Override
    public int getNumberOfControls() {
        return 1;
    }
    
    private void updateUiState() {
        imageSwitcherControl.setSelection(model.isLevelAsImage());
        spinnerControl.setSelection(model.getFontSize());
        tableViewer.setInput(model);
        tableViewer.refresh();
    }
    
    private Button createImageSwitcherControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.FILL);
        composite.setLayout(new GridLayout());
        
        GridData layoutData = new GridData(SWT.BEGINNING, SWT.NONE, true, false);
        composite.setLayoutData(layoutData);
        
        Button button = new Button(composite, SWT.CHECK);
        button.setText("Use image to display log level");
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                boolean levelAsImage = button.getSelection();
                model.setLevelAsImage(levelAsImage);
                tableViewer.refresh();
            }
        });
        return button;
    }
    
    private Spinner createSpinnerBoxControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.FILL);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        composite.setLayout(layout);

        GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        composite.setLayoutData(layoutData);

        Spinner spinner = new Spinner(composite, SWT.BORDER);
        layoutData = new GridData();
        spinner.setLayoutData(layoutData);
        spinner.setMinimum(7);
        spinner.setMaximum(17);
        spinner.setSelection(11);
        spinner.setIncrement(1);
        spinner.setPageIncrement(5);
        spinner.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(final ModifyEvent e) {
                Spinner spinner = (Spinner) e.getSource();
                String value = spinner.getText();
                
                if (!value.isEmpty()) {
                    int fontSize = Integer.parseInt(value);
                    model.setFontSize(fontSize);
                    tableViewer.refresh();
                }
            }
        });
        Label label = new Label(composite, SWT.NONE);
        label.setText("Log's font size");
        return spinner;
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
            case LEVEL_COLUMN_HEADER:
                viewerColumn.setLabelProvider(new LevelColumnLabelProvider(viewer));
                break;
            case FOREGROUND_COLUMN_HEADER:
                viewerColumn.setLabelProvider(new ColorColumnLabelProvider(viewer, SWT.FOREGROUND));
                viewerColumn.setEditingSupport(new ColorColumnCellEditor(viewer, SWT.FOREGROUND));
                break;
            case BACKGROUND_COLUMN_HEADER:
                viewerColumn.setLabelProvider(new ColorColumnLabelProvider(viewer, SWT.BACKGROUND));
                viewerColumn.setEditingSupport(new ColorColumnCellEditor(viewer, SWT.BACKGROUND));
                break;
            default:
                throw new IllegalArgumentException("No column with such a name: " + COLUMN_NAMES[i]);
            }
        }
        Table table = viewer.getTable();
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        return viewer;
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
            PresentationalModel model = (PresentationalModel) inputElement;
            return model.getModelItems().toArray();
        }
    }
    
    private static class LevelColumnLabelProvider extends OwnerDrawLabelProvider {
        
        private final TableViewer viewer;
        
        public LevelColumnLabelProvider(TableViewer viewer) {
            this.viewer = viewer;
        }
        
        @Override
        protected void measure(Event event, Object element) {
            // no code
        }
        
        @Override
        protected void paint(Event event, Object element) {
            TableItem tableItem = (TableItem) event.item;
            PresentationalModelItem modelItem = (PresentationalModelItem) tableItem.getData();
            Rectangle bounds = tableItem.getBounds(event.index);
            PresentationalModel model = (PresentationalModel) viewer.getInput();
            
            if (model.isLevelAsImage()) {
                Image image = ResourceManager.getImage(LogLevel.valueOf(modelItem.getLevelName()));
                Rectangle imageBounds = image.getBounds();
                int xOffset = bounds.width / 2 - imageBounds.width / 2;
                int yOffset = bounds.height / 2 - imageBounds.height / 2;
                int x = xOffset > 0 ? event.x + xOffset : event.x;
                int y = yOffset > 0 ? event.y + yOffset : event.y;
                event.gc.drawImage(image, x, y);
            } else {
                Point point = event.gc.stringExtent(modelItem.getLevelName());
                int xOffset = bounds.width / 2 - point.x / 2;
                int yOffset = bounds.height / 2 - point.y / 2;
                int x = xOffset > 0 ? event.x + xOffset : event.x;
                int y = yOffset > 0 ? event.y + yOffset : event.y;
                event.gc.setFont(ResourceManager.getFont(viewer.getTable(), model.getFontSize(), SWT.NONE));
                event.gc.drawText(modelItem.getLevelName(), x, y, true);
            }
        }
    }

    private static class ColorColumnLabelProvider extends ColumnLabelProvider {
        
        private final TableViewer viewer;

        private int columnType;
        
        public ColorColumnLabelProvider(TableViewer viewer, int columnType) {
            this.viewer = viewer;
            this.columnType = columnType;
        }
        
        @Override
        public String getText(Object element) {
            return "The quick brown fox jumps over the lazy dog";
        }
        
        @Override
        public Font getFont(Object element) {
            PresentationalModel model = (PresentationalModel) viewer.getInput();
            return ResourceManager.getFont(viewer.getTable(), model.getFontSize(), SWT.NONE);
        }

        @Override
        public Color getForeground(Object element) {
            PresentationalModelItem item = (PresentationalModelItem) element;
            return ResourceManager.getColor(Display.getCurrent(), item.getForeground());
        }
        
        @Override
        public Color getBackground(Object element) {
            if (columnType == SWT.BACKGROUND) {
                PresentationalModelItem item = (PresentationalModelItem) element;
                return ResourceManager.getColor(Display.getCurrent(), item.getBackground());
            } else {
                return super.getBackground(element);
            }
        }
    }

    private static class ColorColumnCellEditor extends EditingSupport {
        
        private final TableViewer viewer;
        
        private final int colorType;
        
        private final CellEditor editor;
        
        public ColorColumnCellEditor(TableViewer viewer, int colorType) {
            super(viewer);
            this.viewer = viewer;
            this.colorType = colorType;
            editor = new ColorCellEditor(viewer.getTable());
        }
        
        @Override
        protected CellEditor getCellEditor(Object element) {
            return editor;
        }
        
        @Override
        protected boolean canEdit(Object element) {
            return true;
        }
        
        @Override
        protected Object getValue(Object element) {
            PresentationalModelItem item = (PresentationalModelItem) element;
            Rgb rgb;
            
            if (colorType == SWT.FOREGROUND) {
                rgb = item.getForeground();
            } else {
                rgb = item.getBackground();
            }
            return new RGB(rgb.getRed(), rgb.getGreen(), rgb.getBlue());
        }
        
        @Override
        protected void setValue(Object element, Object value) {
            PresentationalModelItem item = (PresentationalModelItem) element;
            RGB systemRgb = (RGB) value;
            Rgb rgb = new Rgb(systemRgb.red, systemRgb.green, systemRgb.blue);
            
            if (colorType == SWT.FOREGROUND) {
                item.setForeground(rgb);
            } else {
                item.setBackground(rgb);
            }
            viewer.update(item, null);
        }
    }
}
