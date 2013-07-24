package com.rdiachenko.jlv.ui.views;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.rdiachenko.jlv.ui.ConstantUiIds;

public class QuickSearchAction extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IViewPart part = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage()
				.findView(ConstantUiIds.JLV_MAIN_VIEW_ID);

		if (part != null) {
			JlvView view = (JlvView) part;
			view.changeSearchFieldState();
		}
		return null;
	}
}
