package com.github.incode.jlv.ui.views;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.github.incode.jlv.ui.UiStringConstants;
import com.github.incode.jlv.ui.preferences.PreferenceManager;

public class QuickSearchAction extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IViewPart part = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage()
				.findView(UiStringConstants.JLV_LOG_LIST_VIEW_ID);

		if (part != null) {
			LogListView view = (LogListView) part;
			boolean isVisible = !PreferenceManager.isQuickSearchFieldVisible();
			view.setSearchFieldVisible(isVisible);
			PreferenceManager.setQuickSearchFieldVisible(isVisible);
		}
		return null;
	}
}
