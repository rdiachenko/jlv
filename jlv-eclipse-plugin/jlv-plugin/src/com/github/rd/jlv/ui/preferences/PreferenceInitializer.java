package com.github.rd.jlv.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.github.rd.jlv.JlvActivator;
import com.github.rd.jlv.log4j.LogConstants;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = JlvActivator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceManager.SERVER_PORT_NUMBER, 4445);
		store.setDefault(PreferenceManager.SERVER_AUTO_START, true);

		store.setDefault(PreferenceManager.QUICK_SEARCH_FIELD_VISIBLE, true);

		store.setDefault(PreferenceManager.LOGS_BUFFER_SIZE, 1000);
		store.setDefault(PreferenceManager.LOGS_REFRESHING_TIME, 500);

		store.setDefault(PreferenceManager.STRUCTURAL_TABLE_SETTINGS,
				LogConstants.LEVEL_FIELD_NAME + ":55:true;"
						+ LogConstants.CATEGORY_FIELD_NAME + ":100:true;"
						+ LogConstants.MESSAGE_FIELD_NAME + ":100:true;"
						+ LogConstants.LINE_FIELD_NAME + ":100:true;"
						+ LogConstants.DATE_FIELD_NAME + ":100:true;"
						+ LogConstants.THROWABLE_FIELD_NAME + ":100:true;");
		store.setDefault(PreferenceManager.PRESENTATIONAL_TABLE_SETTINGS, "true;11;"
				+ LogConstants.DEBUG_LEVEL_NAME + ":0-0-0:255-255-255;"
				+ LogConstants.INFO_LEVEL_NAME + ":0-255-0:255-255-255;"
				+ LogConstants.WARN_LEVEL_NAME + ":255-128-0:255-255-255;"
				+ LogConstants.ERROR_LEVEL_NAME + ":255-0-0:255-255-255;"
				+ LogConstants.FATAL_LEVEL_NAME + ":165-42-42:255-255-255;");
	}
}
