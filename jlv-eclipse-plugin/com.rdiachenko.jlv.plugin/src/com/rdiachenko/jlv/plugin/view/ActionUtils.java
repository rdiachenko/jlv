package com.rdiachenko.jlv.plugin.view;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.services.ISourceProviderService;

public final class ActionUtils {

    private ActionUtils() {
        // Utility class
    }

    public static SourceProvider getSourceProvider(ExecutionEvent event) {
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
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
