package com.rdiachenko.jlv.plugin;

import java.io.IOException;
import java.net.URL;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

public class JlvActivator extends AbstractUIPlugin {

    private static final String LOGBACK_PROPERTIES_PATH = "resources/logback.xml";

    public static final String PLUGIN_ID = "com.rdiachenko.jlv.plugin";

    private static JlvActivator plugin;

    public JlvActivator() {
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        configureLogging();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
        ResourceManager.dispose();
    }

    public static JlvActivator getDefault() {
        return plugin;
    }
    
    private void configureLogging() throws IOException {
        System.setProperty("JLV_LOG_DIR", getStateLocation().toOSString());
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        try {
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);
            context.reset();
            URL url = getDefault().getBundle().getEntry(LOGBACK_PROPERTIES_PATH);
            configurator.doConfigure(url);
        } catch (JoranException e) {
            // StatusPrinter will handle this
        }
        StatusPrinter.printInCaseOfErrorsOrWarnings(context);
    }
}
