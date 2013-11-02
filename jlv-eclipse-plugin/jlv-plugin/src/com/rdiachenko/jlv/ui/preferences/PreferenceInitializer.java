package com.rdiachenko.jlv.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.rdiachenko.jlv.JlvActivator;
import com.rdiachenko.jlv.model.LogField;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = JlvActivator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceManager.SERVER_PORT_NUMBER, 4445);
		store.setDefault(PreferenceManager.SERVER_AUTO_START, true);

		store.setDefault(PreferenceManager.QUICK_SEARCH_FIELD_STATUS, true);

		store.setDefault(PreferenceManager.LOGS_BUFFER_SIZE, 1000);
		store.setDefault(PreferenceManager.LOGS_REFRESHING_TIME, 500);

		store.setDefault(PreferenceManager.LOGS_TABLE_STRUCTURE_SETTINGS,
				LogField.LEVEL.getName() + ":100:true;"
						+ LogField.CATEGORY.getName() + ":100:true;"
						+ LogField.MESSAGE.getName() + ":100:true;"
						+ LogField.LINE.getName() + ":100:true;"
						+ LogField.DATE.getName() + ":100:true;"
						+ LogField.THROWABLE.getName() + ":100:true;");
		store.setDefault(PreferenceManager.LOGS_TABLE_PRESENTATION_SETTINGS,
				"");
		store.setDefault(PreferenceManager.IMAGE_INSTEAD_TEXT_LEVEL_STATE, true);
		store.setDefault(PreferenceManager.LOGS_FONT_SIZE, 11);
	}
}
