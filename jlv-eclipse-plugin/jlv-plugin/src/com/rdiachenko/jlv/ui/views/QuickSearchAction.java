package com.rdiachenko.jlv.ui.views;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

public class QuickSearchAction implements IViewActionDelegate {

	private JlvView view;

	@Override
	public void run(IAction action) {
		view.changeSearchFieldState();
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// no need to use this event
	}

	@Override
	public void init(IViewPart view) {
		this.view = (JlvView) view;
	}

}
