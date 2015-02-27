package com.github.rd.jlv.eclipse;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.github.rd.jlv.eclipse.ui.preferences.PreferenceManager;
import com.github.rd.jlv.props.JlvProperties;

/**
 * The activator class controls the plug-in life cycle
 *
 * @author <a href="mailto:rd.ryly@gmail.com">Ruslan Diachenko</a>
 */
public class JlvActivator extends AbstractUIPlugin {

	private static final String LOG4J_PROPERTIES_PATH = "config/log4j.properties";

	private static JlvActivator plugin;
	
	private JlvProperties store;

	private PreferenceManager preferenceManager;
	private ResourceManager resourceManager;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		store = new JlvProperties(getStateLocation().toFile());
		resourceManager = new ResourceManager();
		preferenceManager = new PreferenceManager(getPreferenceStore());
		configureLogging();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		try {
			plugin = null;
			store.persist();
			resourceManager.dispose();
		} finally {
			super.stop(context);
		}
	}

	public static JlvActivator getDefault() {
		return plugin;
	}
	
	public JlvProperties getStore() {
		return store;
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
