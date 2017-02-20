package com.rdiachenko.jlv.plugin.action;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.rdiachenko.jlv.plugin.view.LogListView;

public class ClearViewAction extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        LogListView view = ActionUtils.getLogListView(event);

        if (view != null) {
            view.clear();
        }
        return null;
    }
}
