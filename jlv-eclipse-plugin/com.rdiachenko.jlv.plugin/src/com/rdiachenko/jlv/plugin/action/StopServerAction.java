package com.rdiachenko.jlv.plugin.action;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.rdiachenko.jlv.plugin.SourceProvider;
import com.rdiachenko.jlv.plugin.view.LogListView;
import com.rdiachenko.jlv.plugin.view.LogListViewController;

public class StopServerAction extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        LogListView view = ActionUtils.getLogListView(event);

        if (view != null) {
            LogListViewController controller = view.getController();
            controller.stopServer();
            
            SourceProvider sourceProvider = ActionUtils.getSourceProvider(event);
            
            if (sourceProvider != null) {
                sourceProvider.setServerStarted(false);
            }
        }
        return null;
    }
}
