package com.github.rd.jlv;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.github.rd.jlv.ui.preferences.PreferenceManager;

/**
 * The activator class controls the plug-in life cycle
 */
public class JlvActivator extends AbstractUIPlugin {

	private static final String LOG4J_PROPERTIES_PATH = "config/log4j.properties";

	private static JlvActivator plugin;

	private PreferenceManager preferenceManager;

	private ResourceManager resourceManager;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		resourceManager = new ResourceManager();
		preferenceManager = new PreferenceManager(getPreferenceStore());
		configureLogging();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		try {
			plugin = null;
			resourceManager.dispose();
		} finally {
			super.stop(context);
		}
	}

	public static JlvActivator getDefault() {
		return plugin;
	}

	public PreferenceManager getPreferenceManager() {
		return preferenceManager;
	}

	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	private void configureLogging() throws IOException {
		Properties props = new Properties();

		URL url = getDefault().getBundle().getEntry(LOG4J_PROPERTIES_PATH);

		try (FileInputStream configFile = new FileInputStream(FileLocator.toFileURL(url).getFile())) {
			props.load(configFile);
		}

		String logFileLocation = plugin.getStateLocation().toString();
		props.put("log.dir", logFileLocation);
		PropertyConfigurator.configure(props);
	}
}
