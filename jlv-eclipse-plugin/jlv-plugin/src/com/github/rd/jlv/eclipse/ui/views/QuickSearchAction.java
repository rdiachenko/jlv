package com.github.rd.jlv.eclipse.ui.views;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.github.rd.jlv.eclipse.JlvActivator;
import com.github.rd.jlv.eclipse.StringConstants;
import com.github.rd.jlv.props.JlvProperties;
import com.github.rd.jlv.props.PropertyKey;

public class QuickSearchAction extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IViewPart part = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage()
				.findView(StringConstants.JLV_LOG_LIST_VIEW_ID);

		if (part != null) {
			JlvProperties store = JlvActivator.getDefault().getStore();
			PropertyKey key = PropertyKey.LOGLIST_QUICK_SEARCH_KEY; 
			boolean visible = store.load(key);
			store.save(key, !visible);
		}
		return null;
	}
}
