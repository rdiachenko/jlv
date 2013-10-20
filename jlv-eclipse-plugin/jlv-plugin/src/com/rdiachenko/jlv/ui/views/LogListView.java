package com.rdiachenko.jlv.ui.views;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
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

import com.google.common.base.Strings;
import com.rdiachenko.jlv.JlvActivator;
import com.rdiachenko.jlv.log4j.domain.Log;
import com.rdiachenko.jlv.model.LogField;
import com.rdiachenko.jlv.ui.UiStringConstants;
import com.rdiachenko.jlv.ui.preferences.PreferenceManager;

public class LogListView extends ViewPart {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final LogListViewController controller;

	private Text quickSearchField;
	private String quickSearchText;
	private QuickLogFilter quickFilter;

	private TableViewer viewer;

	private ViewLifecycleListener viewLifecycleListener;

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

		if (PreferenceManager.getLogListViewQuickSearchFieldStatus()) {
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
				.getSourceProvider(UiStringConstants.SERVER_STATE_VARIABLE_ID);
		viewSourceProvider.dispose();

		getController().dispose();
		getViewSite().getPage().removePartListener(viewLifecycleListener);
		logger.debug("Lifecycle listener was removed from Jlv log list view");
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
				ViewUtils.openView(UiStringConstants.JLV_LOG_DETAILS_VIEW_ID);
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
		viewer.setLabelProvider(new LogListViewLabelProvider());
		viewer.setInput(getController().getLogContainer());
		viewer.addFilter(quickFilter);

		return viewer;
	}

	private void createColumns(TableViewer viewer) {
		for (final LogField logField : LogField.values()) {
			TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
			TableColumn column = viewerColumn.getColumn();
			column.setText(logField.getName());
			column.setWidth(100);
			viewerColumn.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					Log log = (Log) element;
					String value = logField.getValue(log);
//					logger.debug("Log's field name=value: {}={}", logField.getName(), value);
					return value;
				}
			});
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
			if (JlvActivator.PLUGIN_ID.equals(part.getSite().getPluginId())) {

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
			if (JlvActivator.PLUGIN_ID.equals(part.getSite().getPluginId())) {

				if (part instanceof LogListView) {
					boolean isServerAutoStart = PreferenceManager.getServerAutoStart();
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