package com.rdiachenko.jlv.plugin;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class JlvActivator extends AbstractUIPlugin {
    
    public static final String PLUGIN_ID = "com.rdiachenko.jlv.plugin";
    
    private static JlvActivator plugin;
    
    public JlvActivator() {
    }
    
    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }
    
    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }
    
    public static JlvActivator getDefault() {
        return plugin;
    }
}
