package com.github.rd.jlv.ui.preferences.additional;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColorCellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.OwnerDrawLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
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

import com.github.rd.jlv.JlvActivator;
import com.github.rd.jlv.ResourceManager;
import com.github.rd.jlv.prefs.PreferenceEnum;
import com.github.rd.jlv.prefs.PresentationalModel;
import com.github.rd.jlv.prefs.PresentationalModel.ModelItem;
import com.github.rd.jlv.prefs.PresentationalModel.ModelItem.Rgb;
import com.github.rd.jlv.ui.preferences.PreferenceManager;
import com.github.rd.jlv.ui.preferences.PreferencePageUtils;
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

	private Button imageSwitcherControl;

	private Spinner spinnerControl;

	private TableViewer tableViewer;

	private PresentationalModel model;

	private PreferenceManager preferenceManager;

	private ResourceManager resourceManager;

	public PresentationalTableEditor(String name, Composite parent) {
		init(name, "");
		preferenceManager = JlvActivator.getDefault().getPreferenceManager();
		resourceManager = JlvActivator.getDefault().getResourceManager();
		model = preferenceManager.getDefault(PreferenceEnum.LOG_LIST_PRESENTATIONAL_TABLE_SETTINGS,
				PresentationalModel.class);
		createControl(parent);
	}

	@Override
	public void adjustForNumColumns(int numColumns) {
		((GridData) tableViewer.getControl().getLayoutData()).horizontalSpan = numColumns;
	}

	@Override
	public int getNumberOfControls() {
		return 1;
	}

	@Override
	public void doFillIntoGrid(Composite parent, int numColumns) {
		createImageSwitcherControl(parent);
		createSpinnerBoxControl(parent);

		createTableViewerControl(parent);
		GridData gridData = new GridData();
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		tableViewer.getControl().setLayoutData(gridData);
	}

	@Override
	public void doLoad() {
		doLoad(preferenceManager.getValue(PreferenceEnum.LOG_LIST_PRESENTATIONAL_TABLE_SETTINGS,
				PresentationalModel.class));
	}

	@Override
	public void doLoadDefault() {
		doLoad(preferenceManager.getDefault(PreferenceEnum.LOG_LIST_PRESENTATIONAL_TABLE_SETTINGS,
				PresentationalModel.class));
	}

	@Override
	public void doStore() {
		if (!(tableViewer == null || imageSwitcherControl == null || spinnerControl == null)) {
			preferenceManager.setValue(PreferenceEnum.LOG_LIST_PRESENTATIONAL_TABLE_SETTINGS,
					PresentationalModel.class, model);
		}
	}

	private void doLoad(PresentationalModel model) {
		this.model = model;

		if (!(imageSwitcherControl == null || spinnerControl == null)) {
			imageSwitcherControl.setSelection(this.model.isLevelAsImage());
			spinnerControl.setSelection(this.model.getFontSize());
		}

		if (tableViewer != null) {
			tableViewer.setInput(this.model.getModelItems());
			tableViewer.refresh();
		}
	}

	private void createTableViewerControl(Composite parent) {
		if (tableViewer == null) {
			tableViewer = new TableViewer(parent, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION
					| SWT.HIDE_SELECTION);
			tableViewer.setUseHashlookup(true);
			final Table table = tableViewer.getTable();
			table.setLinesVisible(true);
			table.setHeaderVisible(true);
			table.addDisposeListener(new DisposeListener() {
				@Override
				public void widgetDisposed(DisposeEvent event) {
					tableViewer = null;
				}
			});
			createTableColumns(tableViewer);
			tableViewer.setContentProvider(new ArrayContentProvider());
			tableViewer.setInput(model.getModelItems());
		} else {
			checkParent(tableViewer.getControl(), parent);
		}
	}

	private void createTableColumns(TableViewer tableViewer) {
		for (int i = 0; i < COLUMN_NAMES.length; i++) {
			TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.LEAD);
			viewerColumn.getColumn().setText(COLUMN_NAMES[i]);
			viewerColumn.getColumn().setWidth(COLUMN_WIDTHS[i]);

			switch (COLUMN_NAMES[i]) {
			case LEVEL_COLUMN_HEADER:
				viewerColumn.setLabelProvider(new LevelColumnLabelProvider());
				break;
			case FOREGROUND_COLUMN_HEADER:
				viewerColumn.setLabelProvider(new ColorColumnLabelProvider(SWT.FOREGROUND));
				viewerColumn.setEditingSupport(new ColorColumnCellEditor(tableViewer, SWT.FOREGROUND));
				break;
			case BACKGROUND_COLUMN_HEADER:
				viewerColumn.setLabelProvider(new ColorColumnLabelProvider(SWT.BACKGROUND));
				viewerColumn.setEditingSupport(new ColorColumnCellEditor(tableViewer, SWT.BACKGROUND));
				break;
			default:
				throw new IllegalArgumentException("No column with such name: " + COLUMN_NAMES[i]);
			}
		}
	}

	private void createImageSwitcherControl(Composite parent) {
		if (imageSwitcherControl == null) {
			imageSwitcherControl = PreferencePageUtils.createCheckBoxControl(parent, "Use image to display log level");
			imageSwitcherControl.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					model.setLevelAsImage(imageSwitcherControl.getSelection());
					tableViewer.refresh();
				}
			});
		} else {
			checkParent(imageSwitcherControl, parent);
		}
	}

	private void createSpinnerBoxControl(Composite parent) {
		if (spinnerControl == null) {
			spinnerControl = PreferencePageUtils.createSpinnerControl(parent, "Log's font size");
			spinnerControl.setMinimum(7);
			spinnerControl.setMaximum(17);
			spinnerControl.setSelection(model.getFontSize());
			spinnerControl.setIncrement(1);
			spinnerControl.setPageIncrement(5);
			spinnerControl.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(final ModifyEvent e) {
					Spinner spinner = (Spinner) e.getSource();
					String stringValue = spinner.getText();

					if (!Strings.isNullOrEmpty(stringValue)) {
						model.setFontSize(Integer.parseInt(stringValue));
						updateFontSize(model.getFontSize());
					}
				}
			});
		} else {
			checkParent(spinnerControl, parent);
		}
	}

	private void updateFontSize(int size) {
		Font font = resourceManager.getFont(size);

		for (TableItem item : tableViewer.getTable().getItems()) {
			item.setFont(font);
		}
	}

	private class LevelColumnLabelProvider extends OwnerDrawLabelProvider {

		@Override
		protected void measure(Event event, Object element) {
			// no code
		}

		@Override
		protected void paint(Event event, Object element) {
			TableItem item = (TableItem) event.item;
			ModelItem modelItem = (ModelItem) item.getData();
			Rectangle bounds = item.getBounds(event.index);

			if (model.isLevelAsImage()) {
				Image image = resourceManager.getImage(modelItem.getLevelName());
				Rectangle imageBounds = image.getBounds();
				int xOffset = bounds.width / 2 - imageBounds.width / 2;
				int yOffset = bounds.height / 2 - imageBounds.height / 2;
				int x = xOffset > 0 ? bounds.x + xOffset : bounds.x;
				int y = yOffset > 0 ? bounds.y + yOffset : bounds.y;
				event.gc.drawImage(image, x, y);
			} else {
				Point point = event.gc.stringExtent(modelItem.getLevelName());
				int xOffset = bounds.width / 2 - point.x / 2;
				int yOffset = bounds.height / 2 - point.y / 2;
				int x = xOffset > 0 ? bounds.x + xOffset : bounds.x;
				int y = yOffset > 0 ? bounds.y + yOffset : bounds.y;
				event.gc.drawText(modelItem.getLevelName(), x, y, true);
			}
		}
	}

	private class ColorColumnLabelProvider extends ColumnLabelProvider {

		private int column;

		public ColorColumnLabelProvider(int column) {
			super();
			this.column = column;
		}

		@Override
		public String getText(Object element) {
			return "The quick brown fox jumps over the lazy dog";
		}

		@Override
		public Color getForeground(Object element) {
			ModelItem modelItem = (ModelItem) element;
			return resourceManager.getColor(modelItem.getForeground());
		}

		@Override
		public Color getBackground(Object element) {
			if (column == SWT.BACKGROUND) {
				ModelItem modelItem = (ModelItem) element;
				return resourceManager.getColor(modelItem.getBackground());
			} else {
				return super.getBackground(element);
			}
		}
	}

	private class ColorColumnCellEditor extends EditingSupport {

		private TableViewer viewer;

		private int colorState;

		public ColorColumnCellEditor(TableViewer viewer, int colorState) {
			super(viewer);
			this.viewer = viewer;
			this.colorState = colorState;
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			return new ColorCellEditor(viewer.getTable());
		}

		@Override
		protected boolean canEdit(Object element) {
			return true;
		}

		@Override
		protected Object getValue(Object element) {
			ModelItem modelItem = (ModelItem) element;
			Rgb rgb;

			if (colorState == SWT.FOREGROUND) {
				rgb = modelItem.getForeground();
			} else {
				rgb = modelItem.getBackground();
			}
			return resourceManager.toSystemRgb(rgb);
		}

		@Override
		protected void setValue(Object element, Object value) {
			ModelItem modelItem = (ModelItem) element;
			Rgb rgb = resourceManager.fromSystemRgb((RGB) value);

			if (colorState == SWT.FOREGROUND) {
				modelItem.setForeground(rgb);
			} else {
				modelItem.setBackground(rgb);
			}
			viewer.update(modelItem, null);
		}
	}
}
