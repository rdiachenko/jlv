package com.rdiachenko.jlv.plugin.view;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.AbstractSourceProvider;

public class SourceProvider extends AbstractSourceProvider {
    
    private static final String JLV_SERVER_STARTED_VAR_NAME = "jlvServerStarted";

    @Override
    public void dispose() {
        
    }
    
    @Override
    public Map<String, Boolean> getCurrentState() {
        Map<String, Boolean> state = new HashMap<>();
        state.put(JLV_SERVER_STARTED_VAR_NAME, false);
        return state;
    }
    
    @Override
    public String[] getProvidedSourceNames() {
        return new String[] { JLV_SERVER_STARTED_VAR_NAME };
    }
}
