package com.rdiachenko.jlv.ui.views;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import com.rdiachenko.jlv.ui.ConstantUiIds;

public class JlvDetailedView extends ViewPart implements ISelectionListener {

	@Override
	public void createPartControl(Composite parent) {
		getSite().getPage().addSelectionListener(ConstantUiIds.JLV_MAIN_VIEW_ID, this);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub

	}

}
