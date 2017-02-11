package com.rdiachenko.jlv.plugin.view;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.rdiachenko.jlv.plugin.JlvConstants;

public class QuickSearchAction extends AbstractHandler {
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        String partId = HandlerUtil.getActivePartId(event);
        
        if (JlvConstants.LOGLIST_VIEW_ID.equals(partId)) {
            IViewPart part = (IViewPart) HandlerUtil.getActivePart(event);
            
            if (part != null) {
                LogListView view = (LogListView) part;
                boolean visible = view.isSearchFieldVisible();
                view.setSearchFieldVisible(!visible);
            }
        }
        return null;
    }
}
