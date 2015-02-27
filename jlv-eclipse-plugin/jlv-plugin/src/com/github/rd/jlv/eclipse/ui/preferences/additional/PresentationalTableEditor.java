package com.github.rd.jlv.eclipse.ui.preferences.additional;

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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.github.rd.jlv.eclipse.JlvActivator;
import com.github.rd.jlv.eclipse.ResourceManager;
import com.github.rd.jlv.eclipse.ui.preferences.PreferenceManager;
import com.github.rd.jlv.eclipse.ui.preferences.PreferencePageUtils;
import com.google.common.base.Strings;

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
	private static final int FOREGROUND_COLUMN_WIDTH = 260;
	private static final int BACKGROUND_COLUMN_WIDTH = 260;
	private static final int[] COLUMN_WIDTHS = { LEVEL_COLUMN_WIDTH, FOREGROUND_COLUMN_WIDTH, BACKGROUND_COLUMN_WIDTH };

//	private static final PreferenceEnum TYPE = PreferenceEnum.LOG_LIST_PRESENTATIONAL_TABLE_SETTINGS;
//	private static final Converter CONVERTER = Converter.get(TYPE);

	private Composite topComposite;

	private Button imageSwitcherControl;

	private Spinner spinnerControl;

	private TableViewer tableViewer;

	private PreferenceManager preferenceManager;

	private ResourceManager resourceManager;

//	private PresentationalModel model;

	public PresentationalTableEditor(String name, Composite parent) {
		init(name, "");
		preferenceManager = JlvActivator.getDefault().getPreferenceManager();
		resourceManager = JlvActivator.getDefault().getResourceManager();
		createControl(parent);
	}

	@Override
	public void adjustForNumColumns(int numColumns) {
		((GridData) topComposite.getLayoutData()).horizontalSpan = numColumns;
	}

	@Override
	public int getNumberOfControls() {
		return 1;
	}

	@Override
	public void doFillIntoGrid(Composite parent, int numColumns) {
		topComposite = parent;

		createImageSwitcherControl(parent);
		createSpinnerBoxControl(parent);

		createTableViewerControl(parent);
		GridData gridData = new GridData();
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		tableViewer.getControl().setLayoutData(gridData);
	}

	@Override
	public void doLoad() {
//		model = (PresentationalModel) CONVERTER.jsonToModel(getPreferenceStore().getString(TYPE.getName()));
		init();
	}

	@Override
	public void doLoadDefault() {
//		model = (PresentationalModel) CONVERTER.getDefaultModel();
		init();
	}

	@Override
	public void doStore() {
//		preferenceManager.setValue(PreferenceEnum.LOG_LIST_PRESENTATIONAL_TABLE_SETTINGS, model);
	}

	private void init() {
//		imageSwitcherControl.setSelection(model.isLevelAsImage());
//		spinnerControl.setSelection(model.getFontSize());
//		tableViewer.setInput(model);
		tableViewer.refresh();
	}

	private void createTableViewerControl(Composite parent) {
		tableViewer = new TableViewer(parent, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION
				| SWT.HIDE_SELECTION);
		tableViewer.setUseHashlookup(true);
		final Table table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		createTableColumns(tableViewer);
//		tableViewer.setContentProvider(new ModelContentProvider());
	}

	private void createTableColumns(TableViewer tableViewer) {
		for (int i = 0; i < COLUMN_NAMES.length; i++) {
			TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.LEAD);
			viewerColumn.getColumn().setText(COLUMN_NAMES[i]);
			viewerColumn.getColumn().setWidth(COLUMN_WIDTHS[i]);

			switch (COLUMN_NAMES[i]) {
			case LEVEL_COLUMN_HEADER:
				viewerColumn.setLabelProvider(new LevelColumnLabelProvider(tableViewer));
				break;
			case FOREGROUND_COLUMN_HEADER:
//				viewerColumn.setLabelProvider(new ColorColumnLabelProvider(SWT.FOREGROUND));
//				viewerColumn.setEditingSupport(new ColorColumnCellEditor(tableViewer, SWT.FOREGROUND));
				break;
			case BACKGROUND_COLUMN_HEADER:
//				viewerColumn.setLabelProvider(new ColorColumnLabelProvider(SWT.BACKGROUND));
//				viewerColumn.setEditingSupport(new ColorColumnCellEditor(tableViewer, SWT.BACKGROUND));
				break;
			default:
				throw new IllegalArgumentException("No column with such name: " + COLUMN_NAMES[i]);
			}
		}
	}

	private void createImageSwitcherControl(Composite parent) {
		imageSwitcherControl = PreferencePageUtils.createCheckBoxControl(parent, "Use image to display log level");
		imageSwitcherControl.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				model.setLevelAsImage(imageSwitcherControl.getSelection());
				tableViewer.refresh();
			}
		});
	}

	private void createSpinnerBoxControl(Composite parent) {
		spinnerControl = PreferencePageUtils.createSpinnerControl(parent, "Log's font size");
		spinnerControl.setMinimum(7);
		spinnerControl.setMaximum(17);
		spinnerControl.setSelection(11);
		spinnerControl.setIncrement(1);
		spinnerControl.setPageIncrement(5);
		spinnerControl.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				Spinner spinner = (Spinner) e.getSource();
				String stringValue = spinner.getText();

				if (!Strings.isNullOrEmpty(stringValue)) {
//					model.setFontSize(Integer.parseInt(stringValue));
//					updateFontSize(model.getFontSize());
				}
			}
		});
	}

	private void updateFontSize(int size) {
//		Font font = resourceManager.getFont(size);

		for (TableItem item : tableViewer.getTable().getItems()) {
//			item.setFont(font);
		}
	}

//	private static class ModelContentProvider implements IStructuredContentProvider {
//
//		@Override
//		public void dispose() {
//			// no code
//		}
//
//		@Override
//		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
//			// no code
//		}
//
//		@Override
//		public Object[] getElements(Object inputElement) {
//			PresentationalModel model = (PresentationalModel) inputElement;
//			return model.getModelItems().toArray();
//		}
//	}

	private class LevelColumnLabelProvider extends OwnerDrawLabelProvider {

		private final TableViewer viewer;

		public LevelColumnLabelProvider(TableViewer viewer) {
			super();
			this.viewer = viewer;
		}

		@Override
		protected void measure(Event event, Object element) {
			// no code
		}

		@Override
		protected void paint(Event event, Object element) {
//			TableItem item = (TableItem) event.item;
//			ModelItem modelItem = (ModelItem) item.getData();
//			Rectangle bounds = item.getBounds(event.index);
//			PresentationalModel model = (PresentationalModel) viewer.getInput();
//
//			if (model.isLevelAsImage()) {
//				Image image = resourceManager.getImage(modelItem.getLevelName());
//				Rectangle imageBounds = image.getBounds();
//				int xOffset = bounds.width / 2 - imageBounds.width / 2;
//				int yOffset = bounds.height / 2 - imageBounds.height / 2;
//				int x = xOffset > 0 ? bounds.x + xOffset : bounds.x;
//				int y = yOffset > 0 ? bounds.y + yOffset : bounds.y;
//				event.gc.drawImage(image, x, y);
//			} else {
//				Point point = event.gc.stringExtent(modelItem.getLevelName());
//				int xOffset = bounds.width / 2 - point.x / 2;
//				int yOffset = bounds.height / 2 - point.y / 2;
//				int x = xOffset > 0 ? bounds.x + xOffset : bounds.x;
//				int y = yOffset > 0 ? bounds.y + yOffset : bounds.y;
//				event.gc.drawText(modelItem.getLevelName(), x, y, true);
//			}
		}
	}

//	private class ColorColumnLabelProvider extends ColumnLabelProvider {
//
//		private int columnType;
//
//		public ColorColumnLabelProvider(int columnType) {
//			super();
//			this.columnType = columnType;
//		}
//
//		@Override
//		public String getText(Object element) {
//			return "The quick brown fox jumps over the lazy dog";
//		}
//
//		@Override
//		public Color getForeground(Object element) {
//			ModelItem modelItem = (ModelItem) element;
//			return resourceManager.getColor(modelItem.getForeground());
//		}
//
//		@Override
//		public Color getBackground(Object element) {
//			if (columnType == SWT.BACKGROUND) {
//				ModelItem modelItem = (ModelItem) element;
//				return resourceManager.getColor(modelItem.getBackground());
//			} else {
//				return super.getBackground(element);
//			}
//		}
//	}
//
//	private class ColorColumnCellEditor extends EditingSupport {
//
//		private final TableViewer viewer;
//
//		private final int colorType;
//
//		private final CellEditor editor;
//
//		public ColorColumnCellEditor(TableViewer viewer, int colorType) {
//			super(viewer);
//			this.viewer = viewer;
//			this.colorType = colorType;
//			editor = new ColorCellEditor(viewer.getTable());
//		}
//
//		@Override
//		protected CellEditor getCellEditor(Object element) {
//			return editor;
//		}
//
//		@Override
//		protected boolean canEdit(Object element) {
//			return true;
//		}
//
//		@Override
//		protected Object getValue(Object element) {
//			ModelItem modelItem = (ModelItem) element;
//			Rgb rgb;
//
//			if (colorType == SWT.FOREGROUND) {
//				rgb = modelItem.getForeground();
//			} else {
//				rgb = modelItem.getBackground();
//			}
//			return resourceManager.toSystemRgb(rgb);
//		}
//
//		@Override
//		protected void setValue(Object element, Object value) {
//			ModelItem modelItem = (ModelItem) element;
//			Rgb rgb = resourceManager.fromSystemRgb((RGB) value);
//
//			if (colorType == SWT.FOREGROUND) {
//				modelItem.setForeground(rgb);
//			} else {
//				modelItem.setBackground(rgb);
//			}
//			viewer.update(modelItem, null);
//		}
//	}
}
