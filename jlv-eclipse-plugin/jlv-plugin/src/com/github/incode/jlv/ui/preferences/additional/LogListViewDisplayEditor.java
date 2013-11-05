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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;

import com.github.incode.jlv.JlvActivator;
import com.github.incode.jlv.model.LogLevel;
import com.github.incode.jlv.ui.preferences.PreferenceManager;

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

	private TableViewer tableViewer;

	private LogLevelItem[] tableModel;

	private IPreferenceStore store;

	public LogListViewDisplayEditor(String name, Composite parent) {
		init(name, "");
		this.store = JlvActivator.getDefault().getPreferenceStore();
		createControl(parent);
	}

	@Override
	public void adjustForNumColumns(int numColumns) {
		((GridData) tableViewer.getControl().getLayoutData()).horizontalSpan = numColumns;
	}

	@Override
	public int getNumberOfControls() {
		return 1; // Only one table control
	}

	@Override
	public void doFillIntoGrid(Composite parent, int numColumns) {
		tableViewer = getTableViewerControl(parent);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		tableViewer.getControl().setLayoutData(gridData);
	}

	@Override
	public void doLoad() {
//		doLoad(logsTableStructureManager.loadStructure());
	}

	@Override
	public void doLoadDefault() {
//		doLoad(logsTableStructureManager.loadDefaultStructure());
	}

	@Override
	public void doStore() {
		if (tableViewer != null) {
//			logsTableStructureManager.storeTableStructure(tableModel);
		}
	}

	private void doLoad(LogsTableStructureItem[] model) {
//		if (tableViewer != null) {
//			for (int i = 0; i < model.length; i++) {
//				tableModel[i] = model[i];
//			}
//			tableViewer.refresh();
//		}
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

			createTableColumns(tableViewer);
			tableModel = createTableModel();
			tableViewer.setContentProvider(new ArrayContentProvider());
			tableViewer.setInput(tableModel);
		} else {
			checkParent(tableViewer.getControl(), parent);
		}
		return tableViewer;
	}

	private LogLevelItem[] createTableModel() {
		LogLevelItem[] model = new LogLevelItem[LogLevel.values().length];
		int index = 0;

		for (LogLevel level : LogLevel.values()) {
			LogLevelItem logLevelItem = new LogLevelItem(level.getName(), JlvActivator.getImage(level.image()));
			logLevelItem.setForeground(level.foreground());
			logLevelItem.setBackground(level.background());
			model[index] = logLevelItem;
			index++;
		}
		return model;
	}

	private void createTableColumns(TableViewer tableViewer) {
		for (int i = 0; i < COLUMN_NAMES.length; i++) {
			TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.LEAD);
			viewerColumn.getColumn().setText(COLUMN_NAMES[i]);
			viewerColumn.getColumn().setWidth(COLUMN_WIDTHS[i]);

			switch (COLUMN_NAMES[i]) {
			case LEVEL_LABEL:
				viewerColumn.setLabelProvider(new CustomColumnLabelProvider(CustomColumnLabelProvider.Column.LEVEL));
				break;
			case FOREGROUND_LABEL:
				viewerColumn
						.setLabelProvider(new CustomColumnLabelProvider(CustomColumnLabelProvider.Column.FOREGROUND));
				viewerColumn.setEditingSupport(new CustomColorCellEditor(tableViewer,
						CustomColorCellEditor.ColorState.FOREGROUND));
				break;
			case BACKGROUND_LABEL:
				viewerColumn
						.setLabelProvider(new CustomColumnLabelProvider(CustomColumnLabelProvider.Column.BACKGROUND));
				viewerColumn.setEditingSupport(new CustomColorCellEditor(tableViewer,
						CustomColorCellEditor.ColorState.BACKGROUND));
				break;
			default:
				throw new IllegalArgumentException("No column with such name: " + COLUMN_NAMES[i]
						+ ". Only [Name, Width, Display] are allowed.");
			}
		}
	}

	private static class CustomColumnLabelProvider extends ColumnLabelProvider {

		private boolean imageInsteadOfText;

		private enum Column {
			LEVEL,
			FOREGROUND,
			BACKGROUND
		}

		private Column column;

		public CustomColumnLabelProvider(Column column) {
			super();
			this.column = column;
			imageInsteadOfText = PreferenceManager.getImageInsteadOfTextLevelState();
		}

		@Override
		public String getText(final Object element) {
			if (column == Column.LEVEL) {
				if (imageInsteadOfText) {
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
				if (imageInsteadOfText) {
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