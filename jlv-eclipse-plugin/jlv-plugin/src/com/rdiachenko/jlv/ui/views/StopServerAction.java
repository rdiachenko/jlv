package com.rdiachenko.jlv.ui.views;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StopServerAction implements IViewActionDelegate {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private JlvView view;

	@Override
	public void run(IAction action) {
		view.getController().stopServer();
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		logger.debug("Stop server action selection");
		view.updateServerActionsState();
	}

	@Override
	public void init(IViewPart view) {
		this.view = (JlvView) view;
	}

}
