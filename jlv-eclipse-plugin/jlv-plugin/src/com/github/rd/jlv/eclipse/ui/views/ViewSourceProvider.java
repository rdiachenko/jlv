package com.github.rd.jlv.eclipse.ui.views;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rd.jlv.eclipse.JlvActivator;
import com.github.rd.jlv.eclipse.StringConstants;
import com.github.rd.jlv.props.JlvProperties;

public class ViewSourceProvider extends AbstractSourceProvider {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private JlvProperties store = JlvActivator.getDefault().getStore();

//	private boolean isServerStarted = preferenceManager.isServerAutoStart();

	public void setServerStarted(boolean state) {
//		isServerStarted = state;
//		fireSourceChanged(ISources.WORKBENCH, StringConstants.SERVER_STATE_VARIABLE_ID, isServerStarted);
	}

	@Override
	public void dispose() {
//		setServerStarted(preferenceManager.isServerAutoStart());
		logger.debug("View source provider disposed.");
	}

	@Override
	public Map<String, Boolean> getCurrentState() {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
//		map.put(StringConstants.SERVER_STATE_VARIABLE_ID, isServerStarted);
		return map;
	}

	@Override
	public String[] getProvidedSourceNames() {
		return new String[] { StringConstants.SERVER_STATE_VARIABLE_ID };
	}
}
