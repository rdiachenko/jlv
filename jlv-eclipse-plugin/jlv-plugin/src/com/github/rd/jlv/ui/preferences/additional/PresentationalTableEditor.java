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
import com.github.rd.jlv.model.PresentationalModel;
import com.github.rd.jlv.model.PresentationalModel.ModelItem;
import com.github.rd.jlv.model.PresentationalModel.ModelItem.Rgb;
import com.github.rd.jlv.ui.preferences.PreferenceManager;
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

	private PresentationalModel presentationalModel;

	private PreferenceManager preferenceManager;

	public PresentationalTableEditor(String name, Composite parent) {
		init(name, "");
		preferenceManager = JlvActivator.getDefault().getPreferenceManager();
		presentationalModel = preferenceManager.getDefaultPresentationalModel();
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
		doLoad(preferenceManager.getPresentationalModel());
	}

	@Override
	public void doLoadDefault() {
		doLoad(preferenceManager.getDefaultPresentationalModel());
	}

	@Override
	public void doStore() {
		if (imageSwitcherBox != null && spinnerBox != null && tableViewer != null) {
			preferenceManager.storePresentationalModel(presentationalModel);
		}
	}

	private void doLoad(PresentationalModel model) {
		presentationalModel = model;

		if (imageSwitcherBox != null && spinnerBox != null) {
			imageSwitcherControl.setSelection(presentationalModel.isLevelAsImage());
			spinnerControl.setSelection(presentationalModel.getFontSize());
		}

		if (tableViewer != null) {
			tableViewer.setInput(presentationalModel.getModelItems());
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
			createTableColumns(tableViewer, presentationalModel);
			tableViewer.setContentProvider(new ArrayContentProvider());
			tableViewer.setInput(presentationalModel.getModelItems());
		} else {
			checkParent(tableViewer.getControl(), parent);
		}
		return tableViewer;
	}

	private void createTableColumns(TableViewer tableViewer, PresentationalModel model) {
		for (int i = 0; i < COLUMN_NAMES.length; i++) {
			TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.LEAD);
			viewerColumn.getColumn().setText(COLUMN_NAMES[i]);
			viewerColumn.getColumn().setWidth(COLUMN_WIDTHS[i]);

			switch (COLUMN_NAMES[i]) {
			case LEVEL_COLUMN_HEADER:
				viewerColumn.setLabelProvider(new LevelColumnLabelProvider(model));
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
					presentationalModel.setLevelAsImage(imageSwitcherControl.getSelection());
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
			spinnerControl.setSelection(presentationalModel.getFontSize());
			spinnerControl.setIncrement(1);
			spinnerControl.setPageIncrement(5);
			spinnerControl.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(final ModifyEvent e) {
					Spinner spinner = (Spinner) e.getSource();
					String stringValue = spinner.getText();

					if (!Strings.isNullOrEmpty(stringValue)) {
						presentationalModel.setFontSize(Integer.parseInt(stringValue));
						updateFontSize(presentationalModel.getFontSize());
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
		Display display = Display.getCurrent();

		if (display != null) {
			Font font = JlvActivator.getDefault().getResourceManager().getFont(display, size);

			for (TableItem item : tableViewer.getTable().getItems()) {
				item.setFont(font);
			}
		}
	}

	private class LevelColumnLabelProvider extends OwnerDrawLabelProvider {

		private PresentationalModel model;

		public LevelColumnLabelProvider(PresentationalModel model) {
			this.model = model;
		}

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
				Image image = preferenceManager.getLevelImage(modelItem.getLevelName());
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
			return preferenceManager.getColor(modelItem.getLevelName(), SWT.FOREGROUND);
		}

		@Override
		public Color getBackground(Object element) {
			if (column == SWT.BACKGROUND) {
				ModelItem modelItem = (ModelItem) element;
				return preferenceManager.getColor(modelItem.getLevelName(), SWT.BACKGROUND);
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
			return new RGB(rgb.getRed(), rgb.getGreen(), rgb.getBlue());
		}

		@Override
		protected void setValue(Object element, Object value) {
			ModelItem modelItem = (ModelItem) element;
			Rgb rgb = new Rgb();
			rgb.setRed(((RGB) value).red);
			rgb.setGreen(((RGB) value).green);
			rgb.setBlue(((RGB) value).blue);

			if (colorState == SWT.FOREGROUND) {
				modelItem.setForeground(rgb);
			} else {
				modelItem.setBackground(rgb);
			}
			viewer.update(element, null);
		}
	}
}
