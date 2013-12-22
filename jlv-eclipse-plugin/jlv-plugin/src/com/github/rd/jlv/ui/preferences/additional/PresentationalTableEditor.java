package com.github.rd.jlv.ui.preferences.additional;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
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
import org.eclipse.swt.graphics.FontData;
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

import com.github.rd.jlv.JlvActivator;
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

	private Composite imageSwitcherBox;
	private Button imageSwitcherControl;

	private Composite spinnerBox;
	private Spinner spinnerControl;

	private TableViewer tableViewer;

	private PresentationalPreferenceManager preferenceManager;
	private PresentationalPreferenceModel model;
	private IPreferenceStore store;

	public PresentationalTableEditor(String name, Composite parent) {
		init(name, "");
		this.store = JlvActivator.getDefault().getPreferenceStore();
		preferenceManager = new PresentationalPreferenceManager(store, name);
		model = preferenceManager.loadDefaultModel();
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
		imageSwitcherBox = getImageSwitcherBoxControl(parent);
		spinnerBox = getSpinnerBoxControl(parent);

		tableViewer = getTableViewerControl(parent);
		GridData gridData = new GridData();
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		tableViewer.getControl().setLayoutData(gridData);
	}

	@Override
	public void doLoad() {
		doLoad(preferenceManager.loadModel());
	}

	@Override
	public void doLoadDefault() {
		doLoad(preferenceManager.loadDefaultModel());
	}

	@Override
	public void doStore() {
		if (imageSwitcherBox != null && spinnerBox != null && tableViewer != null) {
			preferenceManager.storeModel(model);
		}
	}

	private void doLoad(PresentationalPreferenceModel presentationalModel) {
		if (imageSwitcherBox != null && spinnerBox != null && tableViewer != null) {
			model.setLevelAsImage(presentationalModel.isLevelAsImage());
			model.setFontSize(presentationalModel.getFontSize());
			model.setLogLevelModelList(presentationalModel.getLogLevelModelList());
			imageSwitcherControl.setSelection(model.isLevelAsImage());
			spinnerControl.setSelection(model.getFontSize());
			tableViewer.refresh();
		}
	}

	private TableViewer getTableViewerControl(Composite parent) {
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
			createTableColumns(tableViewer, model);
			tableViewer.setContentProvider(new ArrayContentProvider());
			tableViewer.setInput(model.getLogLevelModelList());
		} else {
			checkParent(tableViewer.getControl(), parent);
		}
		return tableViewer;
	}

	private void createTableColumns(TableViewer tableViewer, PresentationalPreferenceModel model) {
		for (int i = 0; i < COLUMN_NAMES.length; i++) {
			TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.LEAD);
			viewerColumn.getColumn().setText(COLUMN_NAMES[i]);
			viewerColumn.getColumn().setWidth(COLUMN_WIDTHS[i]);

			switch (COLUMN_NAMES[i]) {
			case LEVEL_COLUMN_HEADER:
				viewerColumn.setLabelProvider(new LevelColumnLabelProvider(model));
				break;
			case FOREGROUND_COLUMN_HEADER:
				viewerColumn.setLabelProvider(
						new ColorColumnLabelProvider(ColorColumnLabelProvider.Column.FOREGROUND));
				viewerColumn.setEditingSupport(new ColorColumnCellEditor(tableViewer,
						ColorColumnCellEditor.ColorState.FOREGROUND));
				break;
			case BACKGROUND_COLUMN_HEADER:
				viewerColumn.setLabelProvider(
						new ColorColumnLabelProvider(ColorColumnLabelProvider.Column.BACKGROUND));
				viewerColumn.setEditingSupport(new ColorColumnCellEditor(tableViewer,
						ColorColumnCellEditor.ColorState.BACKGROUND));
				break;
			default:
				throw new IllegalArgumentException("No column with such name: " + COLUMN_NAMES[i]);
			}
		}
	}

	private Composite getImageSwitcherBoxControl(Composite parent) {
		if (imageSwitcherBox == null) {
			imageSwitcherBox = new Composite(parent, SWT.FILL);
			GridLayout layout = new GridLayout();
			imageSwitcherBox.setLayout(layout);
			GridData layoutData = new GridData(SWT.BEGINNING, SWT.NONE, true, false);
			layoutData.horizontalIndent = -5;
			imageSwitcherBox.setLayoutData(layoutData);
			imageSwitcherBox.addDisposeListener(new DisposeListener() {
				@Override
				public void widgetDisposed(DisposeEvent event) {
					imageSwitcherControl = null;
					imageSwitcherBox = null;
				}
			});

			imageSwitcherControl = new Button(imageSwitcherBox, SWT.CHECK);
			imageSwitcherControl.setText("Use image to display log level");
			imageSwitcherControl.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					model.setLevelAsImage(imageSwitcherControl.getSelection());
					tableViewer.refresh();
				}
			});
		} else {
			checkParent(imageSwitcherBox, parent);
		}
		return imageSwitcherBox;
	}

	private Composite getSpinnerBoxControl(Composite parent) {
		if (spinnerBox == null) {
			spinnerBox = new Composite(parent, SWT.FILL);
			GridLayout layout = new GridLayout();
			layout.numColumns = 2;
			spinnerBox.setLayout(layout);
			GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
			layoutData.horizontalIndent = -5;
			spinnerBox.setLayoutData(layoutData);
			spinnerBox.addDisposeListener(new DisposeListener() {
				@Override
				public void widgetDisposed(DisposeEvent event) {
					spinnerControl = null;
					spinnerBox = null;
				}
			});

			spinnerControl = new Spinner(spinnerBox, SWT.BORDER);
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
			layoutData = new GridData();
			layoutData.widthHint = 20;
			spinnerControl.setLayoutData(layoutData);

			Label label = new Label(spinnerControl.getParent(), SWT.NONE);
			label.setText("Log's font size");
		} else {
			checkParent(spinnerBox, parent);
		}
		return spinnerBox;
	}

	private void updateFontSize(int size) {
		FontData[] fontData = tableViewer.getTable().getFont().getFontData();

		for (int i = 0; i < fontData.length; ++i) {
			fontData[i].setHeight(size);
		}
		Font newFont = new Font(Display.getCurrent(), fontData);

		for (TableItem item : tableViewer.getTable().getItems()) {
			item.setFont(newFont);
		}
	}

	private static class LevelColumnLabelProvider extends OwnerDrawLabelProvider {

		private PresentationalPreferenceModel model;

		public LevelColumnLabelProvider(PresentationalPreferenceModel model) {
			this.model = model;
		}

		@Override
		protected void measure(Event event, Object element) {
			// no code
		}

		@Override
		protected void paint(Event event, Object element) {
			TableItem item = (TableItem) event.item;
			LogLevelModel logLevelItem = (LogLevelModel) item.getData();
			Rectangle bounds = item.getBounds(event.index);

			if (model.isLevelAsImage()) {
				Image image = logLevelItem.getImage();
				Rectangle imageBounds = image.getBounds();
				int xOffset = bounds.width / 2 - imageBounds.width / 2;
				int yOffset = bounds.height / 2 - imageBounds.height / 2;
				int x = xOffset > 0 ? bounds.x + xOffset : bounds.x;
				int y = yOffset > 0 ? bounds.y + yOffset : bounds.y;
				event.gc.drawImage(image, x, y);
			} else {
				Point point = event.gc.stringExtent(logLevelItem.getLevelName());
				int xOffset = bounds.width / 2 - point.x / 2;
				int yOffset = bounds.height / 2 - point.y / 2;
				int x = xOffset > 0 ? bounds.x + xOffset : bounds.x;
				int y = yOffset > 0 ? bounds.y + yOffset : bounds.y;
				event.gc.drawText(logLevelItem.getLevelName(), x, y, true);
			}
		}
	}

	private static class ColorColumnLabelProvider extends ColumnLabelProvider {

		private enum Column {
			FOREGROUND,
			BACKGROUND
		}

		private Column column;

		public ColorColumnLabelProvider(Column column) {
			super();
			this.column = column;
		}

		@Override
		public String getText(Object element) {
			return "The quick brown fox jumps over the lazy dog";
		}

		@Override
		public Color getForeground(Object element) {
			LogLevelModel logLevelItem = (LogLevelModel) element;
			Color color = new Color(Display.getCurrent(), logLevelItem.getForeground());
			return color;
		}

		@Override
		public Color getBackground(Object element) {
			if (column == Column.BACKGROUND) {
				LogLevelModel logLevelItem = (LogLevelModel) element;
				Color color = new Color(Display.getCurrent(), logLevelItem.getBackground());
				return color;
			} else {
				return super.getBackground(element);
			}
		}
	}

	private static class ColorColumnCellEditor extends EditingSupport {

		private enum ColorState {
			FOREGROUND,
			BACKGROUND
		}

		private TableViewer viewer;

		private ColorState colorState;

		public ColorColumnCellEditor(TableViewer viewer, ColorState colorState) {
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
			LogLevelModel logLevelItem = (LogLevelModel) element;

			if (colorState == ColorState.FOREGROUND) {
				return logLevelItem.getForeground();
			} else {
				return logLevelItem.getBackground();
			}
		}

		@Override
		protected void setValue(Object element, Object value) {
			LogLevelModel logLevelItem = (LogLevelModel) element;

			if (colorState == ColorState.FOREGROUND) {
				logLevelItem.setForeground((RGB) value);
			} else {
				logLevelItem.setBackground((RGB) value);
			}
			viewer.update(element, null);
		}
	}
}
