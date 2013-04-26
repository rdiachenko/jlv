package com.rdiachenko.jlv.ui.views;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rdiachenko.jlv.JlvActivator;
import com.rdiachenko.jlv.model.LogField;

public class JlvView extends ViewPart {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static final String START_SERVER_ACTION_ID = "com.rdiachenko.jlv.ui.views.startServerAction";
	private static final String STOP_SERVER_ACTION_ID = "com.rdiachenko.jlv.ui.views.stopServerAction";

	private JlvViewController controller;

	private TableViewer viewer;

	private IAction startServerAction;

	private IAction stopServerAction;

	private ViewLifecycleListener viewLifecycleListener;

	public JlvViewController getController() {
		return controller;
	}

	@Override
	public void createPartControl(Composite parent) {
		viewer = createViewer(parent);
		controller = new JlvViewController(this);

		viewLifecycleListener = new ViewLifecycleListener();
		getViewSite().getPage().addPartListener(viewLifecycleListener);
		logger.debug("Lifecycle listener was added to Jlv view");
	}

	@Override
	public void setFocus() {
		getViewSite().getPage().activate(this);
		viewer.getControl().setFocus();
		logger.debug("Focus has been set on jlv view.");
	}

	@Override
	public void dispose() {
		super.dispose();
		getViewSite().getPage().removePartListener(viewLifecycleListener);
		logger.debug("Lifecycle listener was removed from Jlv view");
	}

	public void clear() {
		viewer.getTable().removeAll();
		logger.debug("Log's table was cleared.");
	}

	public void setStartServerActionEnabled(boolean state) {
		startServerAction.setEnabled(state);
	}

	public void setStopServerActionEnabled(boolean state) {
		stopServerAction.setChecked(state);
	}

	public void updateServerActionsState() {
		logger.debug("Start server action enabled: {}", startServerAction.isEnabled());
		logger.debug("Stop server action enabled: {}", stopServerAction.isEnabled());
		logger.debug("Updating server's action state ...");

		if (startServerAction.isEnabled() && !stopServerAction.isEnabled()) {
			startServerAction.setEnabled(false);
			stopServerAction.setEnabled(true);
		} else if (!startServerAction.isEnabled() && stopServerAction.isEnabled()) {
			startServerAction.setEnabled(true);
			stopServerAction.setEnabled(false);
		}

		logger.debug("Start server action enabled: {}", startServerAction.isEnabled());
		logger.debug("Stop server action enabled: {}", stopServerAction.isEnabled());
	}

	private void initServerActions() {
		startServerAction = ((ActionContributionItem) getViewSite().getActionBars().getToolBarManager()
				.find(START_SERVER_ACTION_ID)).getAction();
		stopServerAction = ((ActionContributionItem) getViewSite().getActionBars().getToolBarManager()
				.find(STOP_SERVER_ACTION_ID)).getAction();
		updateServerActionsState();
	}

	private TableViewer createViewer(Composite parent) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		TableViewer viewer = new TableViewer(parent, style);
		Table table = viewer.getTable();

		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		createColumns(table);

		viewer.setUseHashlookup(true);
		viewer.setContentProvider(new JlvViewContentProvider());
		viewer.setLabelProvider(new JlvViewLabelProvider());
//        viewer.setInput(input);

		return viewer;
	}

	private void createColumns(Table table) {
		for (LogField logField : LogField.values()) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(logField.getName());
			column.setWidth(100);
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

				if (part instanceof JlvView) {
					logger.debug("Jlv view was closed.");
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

				if (part instanceof JlvView) {
					initServerActions();
					logger.debug("Jlv view was opened.");
				}
			}
		}
	}
}