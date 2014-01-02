package com.github.rd.jlv.ui.views;

import java.util.HashMap;
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
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.services.ISourceProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rd.jlv.JlvActivator;
import com.github.rd.jlv.StringConstants;
import com.github.rd.jlv.ui.preferences.PreferenceManager;
import com.github.rd.jlv.ui.preferences.additional.StructuralPreferenceModel;
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
				if (PreferenceManager.STRUCTURAL_TABLE_SETTINGS.equals(event.getProperty())) {
					String structure = event.getNewValue().toString();
					StructuralPreferenceModel[] columnStructure = JlvActivator.getPreferenceManager()
							.getStructuralPreferenceModel(structure);
					updateColumns(viewer.getTable(), columnStructure);
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
		viewer.getControl().setFocus();
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
			viewer.refresh(true, false);
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
		int style = SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION;
		TableViewer viewer = new LogTableViewer(parent, style);
		viewer.getTable().addListener(SWT.Selection, new Listener() {
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
		StructuralPreferenceModel[] columnStructure = JlvActivator.getPreferenceManager()
				.getStructuralPreferenceModel();
		updateColumns(viewer.getTable(), columnStructure);
	}

	private void updateColumns(Table table, StructuralPreferenceModel[] columnStructure) {
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

	private static class ColumnResizeListener implements ControlListener {
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
				JlvActivator.getPreferenceManager().setStructuralPreferenceModel(columnName, width);
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
