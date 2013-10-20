package com.rdiachenko.jlv;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class JlvActivator extends AbstractUIPlugin {

//	private final Logger logger = LoggerFactory.getLogger(getClass());

	public static final String PLUGIN_ID = "com.rdiachenko.jlv.plugin";

	private static final String LOG4J_PROPERTIES_PATH = "config/log4j.properties";

	private static JlvActivator plugin;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		Properties fileProperties = new Properties();
		fileProperties.load(new FileInputStream(getAbsolutePath(LOG4J_PROPERTIES_PATH)));
		String logLocation = plugin.getStateLocation().toString();
		fileProperties.put("log.dir", logLocation);
		PropertyConfigurator.configure(fileProperties);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static JlvActivator getDefault() {
		return plugin;
	}

	public static String getAbsolutePath(String filePath) throws IOException {
		URL confUrl = getDefault().getBundle().getEntry(filePath);
		return FileLocator.toFileURL(confUrl).getFile();
	}

	public static ImageDescriptor getImageDescriptor(String relativePath) {
		return imageDescriptorFromPlugin(PLUGIN_ID, relativePath);
	}
}
