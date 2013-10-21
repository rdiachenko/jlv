package com.rdiachenko.jlv.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.rdiachenko.jlv.JlvActivator;
import com.rdiachenko.jlv.model.LogField;

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
				LogField.LEVEL.getName() + ":100:true;"
						+ LogField.CATEGORY.getName() + ":100:true;"
						+ LogField.MESSAGE.getName() + ":100:true;"
						+ LogField.LINE.getName() + ":100:true;"
						+ LogField.DATE.getName() + ":100:true;"
						+ LogField.THROWABLE.getName() + ":100:true;");
		store.setDefault(PreferenceManager.JLV_LOG_LIST_VIEW_TABLE_PRESENTATION_SETTINGS, "");
	}
}
