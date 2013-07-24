package com.rdiachenko.jlv.ui.views;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;

import com.rdiachenko.jlv.ui.ConstantUiIds;
import com.rdiachenko.jlv.ui.preferences.PreferenceManager;

public class ViewSourceProvider extends AbstractSourceProvider {

	private boolean isServerStarted = PreferenceManager.getServerAutoStart();

	public void setServerStarted(boolean state) {
		isServerStarted = state;
		fireSourceChanged(ISources.WORKBENCH, ConstantUiIds.SERVER_STATE_VARIABLE_ID, isServerStarted);
	}

	@Override
	public void dispose() {
		// no code
	}

	@Override
	public Map<String, Boolean> getCurrentState() {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put(ConstantUiIds.SERVER_STATE_VARIABLE_ID, isServerStarted);
		return map;
	}

	@Override
	public String[] getProvidedSourceNames() {
		return new String[] { ConstantUiIds.SERVER_STATE_VARIABLE_ID };
	}
}
