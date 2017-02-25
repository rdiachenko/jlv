package com.rdiachenko.jlv.plugin.action;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.services.ISourceProviderService;

import com.rdiachenko.jlv.plugin.JlvConstants;
import com.rdiachenko.jlv.plugin.SourceProvider;
import com.rdiachenko.jlv.plugin.view.LogListView;

public final class ActionUtils {

    private ActionUtils() {
        // Utility class
    }

    public static LogListView getLogListView(ExecutionEvent event) {
        String partId = HandlerUtil.getActivePartId(event);
        LogListView view = null;

        if (JlvConstants.LOGLIST_VIEW_ID.equals(partId)) {
            view = (LogListView) HandlerUtil.getActivePart(event);
        }
        return view;
    }
    
    public static SourceProvider getSourceProvider(ExecutionEvent event) {
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
        return getSourceProvider(window);
    }

    public static SourceProvider getSourceProvider(IWorkbenchWindow window) {
        SourceProvider sourceProvider = null;

        if (window != null) {
            ISourceProviderService service = window.getService(ISourceProviderService.class);

            if (service != null) {
                sourceProvider = (SourceProvider) service.getSourceProvider(SourceProvider.JLV_SERVER_STARTED_VAR_NAME);
            }
        }
        return sourceProvider;
    }
}
