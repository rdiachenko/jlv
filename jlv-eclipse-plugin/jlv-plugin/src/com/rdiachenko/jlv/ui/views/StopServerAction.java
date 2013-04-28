package com.rdiachenko.jlv.ui.views;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

public class StopServerAction implements IViewActionDelegate {

	private JlvView view;

	@Override
	public void run(IAction action) {
		view.getController().stopServer();
		view.setStartServerActionEnabled(true);
		view.setStopServerActionEnabled(false);
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
