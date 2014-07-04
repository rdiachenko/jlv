package com.github.rd.jlv.ui.views;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rd.jlv.JlvActivator;
import com.github.rd.jlv.StringConstants;
import com.github.rd.jlv.prefs.PreferenceEnum;
import com.github.rd.jlv.prefs.StructuralModel.ModelItem;
import com.github.rd.jlv.ui.preferences.PreferenceManager;

public class LogListView extends ViewPart {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final LogListViewController controller;
	private final PreferenceManager preferenceManager;

	private Text quickSearchField;
	private String quickSearchText;
	private QuickLogFilter quickFilter;

	private boolean toggledToBottom;

	private TableViewer viewer;
	private Map<String, Integer> columnOrderMap;

	private ViewLifecycleListener viewLifecycleListener;
	private IPropertyChangeListener preferenceListener;

	public LogListView() {
		controller = new LogListViewController(this);
		quickFilter = new QuickLogFilter();
		preferenceManager = JlvActivator.getDefault().getPreferenceManager();
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
				if (PreferenceEnum.LOG_LIST_STRUCTURAL_TABLE_SETTINGS.getName().equals(event.getProperty())) {
					updateColumns(viewer.getTable(), preferenceManager.getStructuralItems());
				}

				if (PreferenceEnum.JLV_GENERAL_SETTINGS.getName().equals(event.getProperty())) {
					setSearchFieldVisible(preferenceManager.isQuickSearchVisible());
				}
			}
		};
		preferenceManager.addPropertyChangeListener(preferenceListener);
		logger.debug("PropertyChange listener was added to Jlv log list view");

		quickSearchField = createQuickSearchField(parent);
		setSearchFieldVisible(preferenceManager.isQuickSearchVisible());
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@Override
	public void dispose() {
		try {
			getController().dispose();
			getViewSite().getPage().removePartListener(viewLifecycleListener);
			logger.debug("Lifecycle listener was removed from Jlv log list view");
			preferenceManager.removePropertyChangeListener(preferenceListener);
			logger.debug("PropertyChange listener was removed from Jlv log list view");
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

	public void clear() {
		viewer.getTable().removeAll();
		logger.debug("log list view was cleared.");
		getController().clearLogContainer();
		logger.debug("Log's container was cleared.");
	}

	public void refreshViewer() {
		if (!viewer.getTable().isDisposed()) {
			viewer.refresh(true, toggledToBottom);

			if (toggledToBottom) {
				Table table = viewer.getTable();
				int itemCount = table.getItemCount();
				table.setSelection(itemCount - 1);
			}
		}
	}

	public void toggleScrollToBottom() {
		toggledToBottom = !toggledToBottom;
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
		int style = SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL;
		TableViewer viewer = new LogTableViewer(parent, style);
		final Table table = viewer.getTable();
//		table.setItemCount(JlvActivator.getPreferenceManager().getLogsBufferSize());
//
//		table.addListener(SWT.SetData, new Listener() {
//			public void handleEvent(Event event) {
//				TableItem item = (TableItem) event.item;
//				item.setText(logs[table.indexOf(item)]);
//			}
//		});

		table.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				ViewUtils.openView(StringConstants.JLV_LOG_DETAILS_VIEW_ID);
				setFocus();
			}
		});
		initColumns(viewer);
		viewer.setInput(getController().getLogContainer());
		viewer.addFilter(quickFilter);
		return viewer;
	}

	private void initColumns(TableViewer viewer) {
		columnOrderMap = new HashMap<>();
		TableColumn[] columns = viewer.getTable().getColumns();

		for (int i = 0; i < columns.length; i++) {
			columns[i].addControlListener(new ColumnResizeListener());
			columnOrderMap.put(columns[i].getText(), i);
		}
		updateColumns(viewer.getTable(), preferenceManager.getStructuralItems());
	}

	private void updateColumns(Table table, List<ModelItem> modelItems) {
		int[] columnOrder = table.getColumnOrder();
		int[] newColumnOrder = new int[columnOrder.length];
		int index = 0;

		for (ModelItem item : modelItems) {
			int position = columnOrderMap.get(item.getName());
			newColumnOrder[index] = columnOrder[position];
			columnOrderMap.put(item.getName(), index);

			TableColumn column = table.getColumn(newColumnOrder[index]);

			if (item.isDisplay()) {
				column.setWidth(item.getWidth());
			} else {
				column.setWidth(0);
			}
			++index;
		}
		table.setColumnOrder(newColumnOrder);
		table.redraw();
	}

	private class ColumnResizeListener implements ControlListener {

		private long startTime = System.currentTimeMillis();

		@Override
		public void controlMoved(ControlEvent e) {
			// no code
		}

		@Override
		public void controlResized(ControlEvent e) {
			if (e.getSource() instanceof TableColumn) {
				TableColumn column = (TableColumn) e.getSource();
				String columnName = column.getText();
				int width = column.getWidth();
				long endTime = System.currentTimeMillis();

				if (endTime - startTime > 1000) {
					preferenceManager.storeColumnWidth(columnName, width);
				}
				startTime = System.currentTimeMillis();
			}
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
					boolean isServerAutoStart = preferenceManager.isServerAutoStart();
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
