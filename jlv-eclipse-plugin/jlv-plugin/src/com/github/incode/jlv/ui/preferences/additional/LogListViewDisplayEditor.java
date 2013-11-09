package com.github.incode.jlv.ui.preferences.additional;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColorCellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
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
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.github.incode.jlv.JlvActivator;
import com.github.incode.jlv.model.LogLevel;
import com.google.common.base.Strings;

public class LogListViewDisplayEditor extends FieldEditor {

//	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static final String LEVEL_LABEL = "Level";
	private static final String FOREGROUND_LABEL = "Foreground";
	private static final String BACKGROUND_LABEL = "Background";
	private static final String[] COLUMN_NAMES = { LEVEL_LABEL, FOREGROUND_LABEL, BACKGROUND_LABEL };

	private static final int NAME_COLUMN_WIDTH = 60;
	private static final int WIDTH_COLUMN_WIDTH = 300;
	private static final int DISPLAY_COLUMN_WIDTH = 300;
	private static final int[] COLUMN_WIDTHS = { NAME_COLUMN_WIDTH, WIDTH_COLUMN_WIDTH, DISPLAY_COLUMN_WIDTH };

	private Composite imageSwitcherBox;
	private Button imageSwitcher;

	private Composite spinnerBox;
	private Spinner spinner;

	private TableViewer tableViewer;

	private LogsDisplayModel model;

	private IPreferenceStore store;
	private LogsDisplayPreferenceManager preferenceManager;

	public LogListViewDisplayEditor(String name, Composite parent) {
		init(name, "");
		this.store = JlvActivator.getDefault().getPreferenceStore();
		preferenceManager = new LogsDisplayPreferenceManager(store, name);
		model = createModel();
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

	private void doLoad(LogsDisplayModel ldModel) {
		if (imageSwitcherBox != null && spinnerBox != null && tableViewer != null) {
			model.setLevelImageSubstitutesText(ldModel.isLevelImageSubstitutesText());
			model.setFontSize(ldModel.getFontSize());
			model.setLogLevelItems(ldModel.getLogLevelItems());

			imageSwitcher.setSelection(model.isLevelImageSubstitutesText());
			spinner.setSelection(model.getFontSize());
			tableViewer.refresh();
		}
	}

	private TableViewer getTableViewerControl(Composite parent) {
		if (tableViewer == null) {
			tableViewer = new TableViewer(parent, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION
					| SWT.HIDE_SELECTION);
			tableViewer.setUseHashlookup(true);
			Table table = tableViewer.getTable();
			table.setLinesVisible(true);
			table.setHeaderVisible(true);
			table.addDisposeListener(new DisposeListener() {
				public void widgetDisposed(DisposeEvent event) {
					tableViewer = null;
				}
			});

			createTableColumns(tableViewer, model);
			tableViewer.setContentProvider(new ArrayContentProvider());
			tableViewer.setInput(model.getLogLevelItems());
		} else {
			checkParent(tableViewer.getControl(), parent);
		}
		return tableViewer;
	}

	private LogsDisplayModel createModel() {
		LogLevelItem[] logLevelItems = new LogLevelItem[LogLevel.values().length];
		int index = 0;

		for (LogLevel level : LogLevel.values()) {
			LogLevelItem logLevelItem = new LogLevelItem(level.getName(), JlvActivator.getImage(level.image()),
					level.foreground(), level.background());
			logLevelItems[index] = logLevelItem;
			index++;
		}
		LogsDisplayModel model = new LogsDisplayModel(true, 11, logLevelItems);
		return model;
	}

	private void createTableColumns(TableViewer tableViewer, LogsDisplayModel model) {
		for (int i = 0; i < COLUMN_NAMES.length; i++) {
			TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.LEAD);
			viewerColumn.getColumn().setText(COLUMN_NAMES[i]);
			viewerColumn.getColumn().setWidth(COLUMN_WIDTHS[i]);

			switch (COLUMN_NAMES[i]) {
			case LEVEL_LABEL:
				viewerColumn.setLabelProvider(new CustomColumnLabelProvider(CustomColumnLabelProvider.Column.LEVEL,
						model));
				break;
			case FOREGROUND_LABEL:
				viewerColumn.setLabelProvider(
						new CustomColumnLabelProvider(CustomColumnLabelProvider.Column.FOREGROUND, model));
				viewerColumn.setEditingSupport(new CustomColorCellEditor(tableViewer,
						CustomColorCellEditor.ColorState.FOREGROUND));
				break;
			case BACKGROUND_LABEL:
				viewerColumn.setLabelProvider(
						new CustomColumnLabelProvider(CustomColumnLabelProvider.Column.BACKGROUND, model));
				viewerColumn.setEditingSupport(new CustomColorCellEditor(tableViewer,
						CustomColorCellEditor.ColorState.BACKGROUND));
				break;
			default:
				throw new IllegalArgumentException("No column with such name: " + COLUMN_NAMES[i]
						+ ". Only [Name, Width, Display] are allowed.");
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
				public void widgetDisposed(DisposeEvent event) {
					imageSwitcher = null;
					imageSwitcherBox = null;
				}
			});

			imageSwitcher = new Button(imageSwitcherBox, SWT.CHECK);
			imageSwitcher.setText("Use image to display log level");
			imageSwitcher.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					model.setLevelImageSubstitutesText(imageSwitcher.getSelection());
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
			GridData layoutData = new GridData(SWT.BEGINNING, SWT.NONE, true, false);
			layoutData.horizontalIndent = -5;
			spinnerBox.setLayoutData(layoutData);
			spinnerBox.addDisposeListener(new DisposeListener() {
				public void widgetDisposed(DisposeEvent event) {
					spinner = null;
					spinnerBox = null;
				}
			});

			spinner = new Spinner(spinnerBox, SWT.BORDER);
			spinner.setMinimum(7);
			spinner.setMaximum(17);
			spinner.setSelection(model.getFontSize());
			spinner.setIncrement(1);
			spinner.setPageIncrement(5);
			spinner.addModifyListener(new ModifyListener() {
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

			Label label = new Label(spinner.getParent(), SWT.NONE);
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

	private static class CustomColumnLabelProvider extends ColumnLabelProvider {

		private LogsDisplayModel model;

		private enum Column {
			LEVEL,
			FOREGROUND,
			BACKGROUND
		}

		private Column column;

		public CustomColumnLabelProvider(Column column, LogsDisplayModel model) {
			super();
			this.column = column;
			this.model = model;
		}

		@Override
		public String getText(final Object element) {
			if (column == Column.LEVEL) {
				if (model.isLevelImageSubstitutesText()) {
					return null;
				} else {
					LogLevelItem logLevelItem = (LogLevelItem) element;
					return logLevelItem.getName();
				}
			} else {
				return "The quick brown fox jumps over the lazy dog";
			}
		}

		@Override
		public Image getImage(final Object element) {
			if (column == Column.LEVEL) {
				if (model.isLevelImageSubstitutesText()) {
					LogLevelItem logLevelItem = (LogLevelItem) element;
					return logLevelItem.getImage();
				} else {
					return null;
				}
			} else {
				return super.getImage(element);
			}
		}

		@Override
		public Color getForeground(final Object element) {
			if (column == Column.LEVEL) {
				return super.getForeground(element);
			} else {
				LogLevelItem logLevelItem = (LogLevelItem) element;
				Color color = new Color(Display.getCurrent(), logLevelItem.getForeground());
				return color;
			}
		}

		@Override
		public Color getBackground(final Object element) {
			if (column == Column.BACKGROUND) {
				LogLevelItem logLevelItem = (LogLevelItem) element;
				Color color = new Color(Display.getCurrent(), logLevelItem.getBackground());
				return color;
			} else {
				return super.getBackground(element);
			}
		}
	}

	private static class CustomColorCellEditor extends EditingSupport {

		private enum ColorState {
			FOREGROUND,
			BACKGROUND
		}

		private TableViewer viewer;

		private ColorState colorState;

		public CustomColorCellEditor(TableViewer viewer, ColorState colorState) {
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
			LogLevelItem logLevelItem = (LogLevelItem) element;

			if (colorState == ColorState.FOREGROUND) {
				return logLevelItem.getForeground();
			} else {
				return logLevelItem.getBackground();
			}
		}

		@Override
		protected void setValue(final Object element, final Object value) {
			LogLevelItem logLevelItem = (LogLevelItem) element;

			if (colorState == ColorState.FOREGROUND) {
				logLevelItem.setForeground((RGB) value);
			} else {
				logLevelItem.setBackground((RGB) value);
			}
			viewer.update(element, null);
		}
	}
}