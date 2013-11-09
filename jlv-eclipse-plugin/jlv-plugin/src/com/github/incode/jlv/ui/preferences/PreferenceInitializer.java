package com.github.incode.jlv.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.github.incode.jlv.JlvActivator;
import com.github.incode.jlv.model.LogField;
import com.github.incode.jlv.model.LogLevel;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = JlvActivator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceManager.SERVER_PORT_NUMBER, 4445);
		store.setDefault(PreferenceManager.SERVER_AUTO_START, true);

		store.setDefault(PreferenceManager.QUICK_SEARCH_FIELD_VISIBLE, true);

		store.setDefault(PreferenceManager.LOGS_BUFFER_SIZE, 1000);
		store.setDefault(PreferenceManager.LOGS_REFRESHING_TIME, 500);

		store.setDefault(PreferenceManager.LOGS_TABLE_STRUCTURE_SETTINGS,
				LogField.LEVEL.getName() + ":100:true;"
						+ LogField.CATEGORY.getName() + ":100:true;"
						+ LogField.MESSAGE.getName() + ":100:true;"
						+ LogField.LINE.getName() + ":100:true;"
						+ LogField.DATE.getName() + ":100:true;"
						+ LogField.THROWABLE.getName() + ":100:true;");
		store.setDefault(PreferenceManager.LOGS_TABLE_PRESENTATION_SETTINGS, "true;11;"
				+ LogLevel.DEBUG.getName() + ":0-0-0:237-236-235;"
				+ LogLevel.INFO.getName() + ":0-255-0:237-236-235;"
				+ LogLevel.WARN.getName() + ":255-128-0:237-236-235;"
				+ LogLevel.ERROR.getName() + ":255-0-0:237-236-235;"
				+ LogLevel.FATAL.getName() + ":165-42-42:237-236-235;");
	}
}
