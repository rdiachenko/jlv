package com.rdiachenko.jlv.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.rdiachenko.jlv.JlvActivator;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = JlvActivator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceManager.JLV_SERVER_PORT_NUMBER, 4445);
		store.setDefault(PreferenceManager.JLV_SERVER_AUTO_START, true);

		store.setDefault(PreferenceManager.JLV_LOG_LIST_VIEW_QUICK_SEARCH_FIELD_STATUS, true);

		store.setDefault(PreferenceManager.JLV_LOG_LIST_VIEW_BUFFER_SIZE, 1000);
		store.setDefault(PreferenceManager.JLV_LOG_LIST_VIEW_REFRESHING_TIME, 500);

		store.setDefault(PreferenceManager.JLV_LOG_LIST_VIEW_TABLE_STRUCTURE_SETTINGS,
				"0:level:100:true;"
						+ "1:category:100:true;"
						+ "2:message:100:true;"
						+ "3:line:100:true;"
						+ "4:date:100:true;"
						+ "5:throwable:100:true;");
		store.setDefault(PreferenceManager.JLV_LOG_LIST_VIEW_TABLE_PRESENTATION_SETTINGS, "");
	}
}
