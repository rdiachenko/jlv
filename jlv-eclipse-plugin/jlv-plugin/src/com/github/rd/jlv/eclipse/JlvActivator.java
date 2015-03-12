package com.github.rd.jlv.eclipse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.github.rd.jlv.props.JlvProperties;
import com.google.common.base.Strings;

/**
 * The activator class controls the plug-in life cycle
 *
 * @author <a href="mailto:rd.ryly@gmail.com">Ruslan Diachenko</a>
 */
public class JlvActivator extends AbstractUIPlugin {

	private static final String LOG4J_PROPERTIES_PATH = "config/log4j.properties";

	private static JlvActivator plugin;

	private JlvProperties store;
	private ResourceManager resourceManager;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		configureLogging();
		configureStore();
		resourceManager = new ResourceManager();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		try {
			store.persist();
			resourceManager.dispose();
			plugin = null;
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

	public ResourceManager getResourceManager() {
		return resourceManager;
	}
	
	private void configureStore() {
		String pluginStateLocation = getStateLocation().toFile().getAbsolutePath();
		
		if (Strings.isNullOrEmpty(pluginStateLocation)) {
			pluginStateLocation = System.getProperty("java.io.tmpdir", ".");
		}
		File jlvPropertiesFile = new File(pluginStateLocation + File.separator + "jlv.properties");
		store = new JlvProperties(jlvPropertiesFile);
	}

	private void configureLogging() throws IOException {
		Properties props = new Properties();
		URL url = getDefault().getBundle().getEntry(LOG4J_PROPERTIES_PATH);

		try (FileInputStream configFile = new FileInputStream(FileLocator.toFileURL(url).getFile())) {
			props.load(configFile);
		}
		props.put("log.dir", getStateLocation().toFile().getAbsolutePath());
		PropertyConfigurator.configure(props);
	}
}
