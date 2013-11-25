package com.github.rd.jlv.ui.views;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.services.ISourceProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rd.jlv.JlvActivator;
import com.github.rd.jlv.StringConstants;
import com.github.rd.jlv.log4j.domain.Log;
import com.github.rd.jlv.model.LogField;
import com.github.rd.jlv.model.LogLevel;
import com.github.rd.jlv.ui.preferences.PreferenceManager;
import com.github.rd.jlv.ui.preferences.additional.LogsTableStructureItem;
import com.google.common.base.Strings;

public class LogListView extends ViewPart {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final LogListViewController controller;

	private Text quickSearchField;
	private String quickSearchText;
	private QuickLogFilter quickFilter;

	private TableViewer viewer;
	private Map<String, Integer> columnOrderMap;

	private ViewLifecycleListener viewLifecycleListener;
	private IPropertyChangeListener preferenceListener;

	public LogListView() {
		controller = new LogListViewController(this);
		quickFilter = new QuickLogFilter();
	}

	public LogListViewController getController() {
		return controller;
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.verticalSpacing = 0;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		parent.setLayout(layout);

		viewer = createViewer(parent);
		getViewSite().setSelectionProvider(viewer);

		viewLifecycleListener = new ViewLifecycleListener();
		getViewSite().getPage().addPartListener(viewLifecycleListener);
		logger.debug("Lifecycle listener was added to Jlv log list view");

		preferenceListener = new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (PreferenceManager.LOGS_TABLE_STRUCTURE_SETTINGS.equals(event.getProperty())) {
					String structure = event.getNewValue().toString();
					LogsTableStructureItem[] columnStructure = JlvActivator.getPreferenceManager()
							.getLogsTableStructure(structure);
					updateColumns(columnStructure);
				}
			}
		};
		JlvActivator.getPreferenceManager().addPropertyChangeListener(preferenceListener);
		logger.debug("PropertyChange listener was added to Jlv log list view");

		if (JlvActivator.getPreferenceManager().isQuickSearchFieldVisible()) {
			quickSearchField = createQuickSearchField(parent);
		}
	}

	@Override
	public void setFocus() {
		getViewSite().getPage().activate(this);
	}

	@Override
	public void dispose() {
		super.dispose();

		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		ISourceProviderService service = (ISourceProviderService) window.getService(ISourceProviderService.class);
		ViewSourceProvider viewSourceProvider = (ViewSourceProvider) service
				.getSourceProvider(StringConstants.SERVER_STATE_VARIABLE_ID);
		viewSourceProvider.dispose();

		getController().dispose();
		getViewSite().getPage().removePartListener(viewLifecycleListener);
		logger.debug("Lifecycle listener was removed from Jlv log list view");

		JlvActivator.getPreferenceManager().removePropertyChangeListener(preferenceListener);
		logger.debug("PropertyChange listener was removed from Jlv log list view");
	}

	public void setSearchFieldVisible(boolean isVisible) {
		Composite parent = viewer.getTable().getParent();

		if (isVisible) {
			quickSearchField = createQuickSearchField(parent);

			if (!Strings.isNullOrEmpty(quickSearchText)) {
				quickSearchField.setText(quickSearchText);
				quickSearchField.selectAll();
			}
			quickSearchField.setFocus();
		} else {
			quickSearchText = quickSearchField.getText();
			quickSearchField.dispose();
		}
		parent.layout();
	}

	public void clear() {
		viewer.getTable().removeAll();
		logger.debug("log list view was cleared.");
		getController().clearLogContainer();
		logger.debug("Log's container was cleared.");
	}

	public void refreshViewer() {
		if (!viewer.getTable().isDisposed()) {
			viewer.refresh();
		}
	}

	private Text createQuickSearchField(Composite parent) {
		final Text quickSearchField = new Text(parent, SWT.BORDER);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		quickSearchField.setLayoutData(gridData);

		quickSearchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent key) {
				quickFilter.setSearchText(quickSearchField.getText());
				refreshViewer();
			}
		});
		return quickSearchField;
	}

	private TableViewer createViewer(Composite parent) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION | SWT.HIDE_SELECTION | SWT.VIRTUAL;
		TableViewer viewer = new TableViewer(parent, style);
		Table table = viewer.getTable();

		table.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				ViewUtils.openView(StringConstants.JLV_LOG_DETAILS_VIEW_ID);
				setFocus();
			}
		});

		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		createColumns(viewer);

		viewer.setUseHashlookup(true);
		viewer.setContentProvider(new LogListViewContentProvider());
		viewer.setInput(getController().getLogContainer());
		viewer.addFilter(quickFilter);

		return viewer;
	}

	private void createColumns(TableViewer viewer) {
		columnOrderMap = new HashMap<>();
		LogsTableStructureItem[] columnStructure = JlvActivator.getPreferenceManager().getLogsTableStructure();

		for (int i = 0; i < columnStructure.length; i++) {
			TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.LEAD);
			TableColumn column = viewerColumn.getColumn();
			column.setText(columnStructure[i].getName());
			columnOrderMap.put(columnStructure[i].getName(), i);

			if (columnStructure[i].isDisplay()) {
				column.setWidth(columnStructure[i].getWidth());
			} else {
				column.setWidth(0);
			}
			LogField logField = LogField.getLogFieldByName(columnStructure[i].getName());
			viewerColumn.setLabelProvider(new CustomColumnLabelProvider(viewer.getTable(), logField));
		}
	}

	private void updateColumns(LogsTableStructureItem[] columnStructure) {
		Table table = viewer.getTable();
		int[] columnOrder = table.getColumnOrder();
		int[] newColumnOrder = new int[columnOrder.length];

		for (int i = 0; i < columnStructure.length; i++) {
			int position = columnOrderMap.get(columnStructure[i].getName());
			newColumnOrder[i] = columnOrder[position];
			columnOrderMap.put(columnStructure[i].getName(), i);

			TableColumn column = table.getColumn(newColumnOrder[i]);

			if (columnStructure[i].isDisplay()) {
				column.setWidth(columnStructure[i].getWidth());
			} else {
				column.setWidth(0);
			}
		}
		table.setColumnOrder(newColumnOrder);
		table.redraw();
	}

	private static class CustomColumnLabelProvider extends ColumnLabelProvider {

		private Table table;

		private LogField logField;

		public CustomColumnLabelProvider(Table table, LogField logField) {
			super();
			this.table = table;
			this.logField = logField;
		}

		@Override
		public String getText(Object element) {
			Log log = (Log) element;

			switch (logField) {
			case MESSAGE:
			case THROWABLE:
				String value = logField.getValue(log);
				value = value.replaceAll("\\r|\\n", " ");
				int textLengthLimit = 200;

				if (value.length() > textLengthLimit) {
					value = value.substring(0, textLengthLimit) + "...";
				}
				return value;
			case LEVEL:
				if (JlvActivator.getPreferenceManager().isLevelImageSubstitutesText()) {
					return null;
				} else {
					return log.getLevel();
				}
			default:
				return logField.getValue(log);
			}
		}

		@Override
		public Image getImage(Object element) {
			Log log = (Log) element;

			if (logField == LogField.LEVEL) {
				if (JlvActivator.getPreferenceManager().isLevelImageSubstitutesText()) {
					return LogLevel.getImageByName(log.getLevel());
				} else {
					return null;
				}
			} else {
				return null;
			}
		}

		@Override
		public Color getForeground(Object element) {
			Log log = (Log) element;
			RGB rgb = JlvActivator.getPreferenceManager().getLogsForeground(
					LogLevel.getLogLevelByName(log.getLevel()));
			Color color = new Color(Display.getCurrent(), rgb);
			return color;
		}

		@Override
		public Color getBackground(Object element) {
			Log log = (Log) element;
			RGB rgb = JlvActivator.getPreferenceManager().getLogsBackground(
					LogLevel.getLogLevelByName(log.getLevel()));
			Color color = new Color(Display.getCurrent(), rgb);
			return color;
		}

		@Override
		public Font getFont(Object element) {
			FontData[] fontData = table.getFont().getFontData();

			for (int i = 0; i < fontData.length; ++i) {
				fontData[i].setHeight(JlvActivator.getPreferenceManager().getLogsFontSize());
			}
			Font font = new Font(Display.getCurrent(), fontData);
			return font;
		}
	}

	// Inner class represents handler for LogView life cycle actions: close/open view, activate/deactivate view, etc.
	private class ViewLifecycleListener implements IPartListener {

		@Override
		public void partActivated(final IWorkbenchPart part) {
			// no code
		}

		@Override
		public void partBroughtToTop(final IWorkbenchPart part) {
			// no code
		}

		@Override
		public void partClosed(final IWorkbenchPart part) {
			if (StringConstants.JLV_PLUGIN_ID.equals(part.getSite().getPluginId())) {

				if (part instanceof LogListView) {
					logger.debug("Jlv log list view was closed.");
				}
			}
		}

		@Override
		public void partDeactivated(final IWorkbenchPart part) {
			// no code
		}

		@Override
		public void partOpened(final IWorkbenchPart part) {
			if (StringConstants.JLV_PLUGIN_ID.equals(part.getSite().getPluginId())) {

				if (part instanceof LogListView) {
					boolean isServerAutoStart = JlvActivator.getPreferenceManager().isServerAutoStart();
					logger.debug("Server auto start option: {}", isServerAutoStart);

					if (isServerAutoStart) {
						getController().startServer();
					}
					logger.debug("Jlv log list view was opened.");
				}
			}
		}
	}
}
