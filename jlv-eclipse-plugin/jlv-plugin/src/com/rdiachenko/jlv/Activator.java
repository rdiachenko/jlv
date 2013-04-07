package com.rdiachenko.jlv;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "com.rdiachenko.jlv";

	public static final String LOG4J_CONFIG_KEY = "log4j.configuration";

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static Activator plugin;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		if (System.getProperties().containsKey(LOG4J_CONFIG_KEY)) {
			String file = System.getProperties().getProperty(LOG4J_CONFIG_KEY);
			PropertyConfigurator.configure(file);
			logger.debug("Log4j's configuration was loaded from: {}", file);
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static Activator getDefault() {
		return plugin;
	}
}
