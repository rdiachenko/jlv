package com.github.rd.jlv.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.github.rd.jlv.JlvActivator;
import com.github.rd.jlv.prefs.Converter;
import com.github.rd.jlv.prefs.Model;
import com.github.rd.jlv.prefs.PreferenceEnum;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		setDefault(PreferenceEnum.JLV_GENERAL_SETTINGS);
		setDefault(PreferenceEnum.LOG_LIST_STRUCTURAL_TABLE_SETTINGS);
		setDefault(PreferenceEnum.LOG_LIST_PRESENTATIONAL_TABLE_SETTINGS);
	}

	private void setDefault(PreferenceEnum preference) {
		Converter converter = Converter.get(preference);
		Model model = converter.getDefaultModel();
		IPreferenceStore store = JlvActivator.getDefault().getPreferenceStore();
		store.setDefault(preference.getName(), converter.modelToJson(model));
	}
}
