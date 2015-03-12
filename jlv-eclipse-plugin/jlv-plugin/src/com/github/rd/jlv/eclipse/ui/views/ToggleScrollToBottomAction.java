package com.github.rd.jlv.eclipse.ui.views;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.github.rd.jlv.eclipse.StringConstants;

public class ToggleScrollToBottomAction extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IViewPart part = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage()
				.findView(StringConstants.JLV_LOG_LIST_VIEW_ID);

		if (part != null) {
			LoglistView view = (LoglistView) part;
			view.toggleScrollToBottom();
		}
		return null;
	}
}
