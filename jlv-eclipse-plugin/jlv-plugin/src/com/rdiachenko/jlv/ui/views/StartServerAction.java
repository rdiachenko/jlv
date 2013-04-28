package com.rdiachenko.jlv.ui.views;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

public class StartServerAction implements IViewActionDelegate {

	private JlvView view;

	@Override
	public void run(IAction action) {
		view.getController().startServer();
		view.setStartServerActionEnabled(false);
		view.setStopServerActionEnabled(true);
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// no code
	}

	@Override
	public void init(IViewPart view) {
		this.view = (JlvView) view;
	}
}
