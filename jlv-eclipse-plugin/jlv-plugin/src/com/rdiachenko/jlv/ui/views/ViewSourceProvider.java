package com.rdiachenko.jlv.ui.views;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rdiachenko.jlv.ui.UiStringConstants;
import com.rdiachenko.jlv.ui.preferences.PreferenceManager;

public class ViewSourceProvider extends AbstractSourceProvider {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private boolean isServerStarted = PreferenceManager.getServerAutoStart();

	public void setServerStarted(boolean state) {
		isServerStarted = state;
		fireSourceChanged(ISources.WORKBENCH, UiStringConstants.SERVER_STATE_VARIABLE_ID, isServerStarted);
	}

	@Override
	public void dispose() {
		setServerStarted(PreferenceManager.getServerAutoStart());
		logger.debug("View source provider has been disposed.");
	}

	@Override
	public Map<String, Boolean> getCurrentState() {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put(UiStringConstants.SERVER_STATE_VARIABLE_ID, isServerStarted);
		return map;
	}

	@Override
	public String[] getProvidedSourceNames() {
		return new String[] { UiStringConstants.SERVER_STATE_VARIABLE_ID };
	}
}
