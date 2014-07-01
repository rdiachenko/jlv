package com.github.rd.jlv.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;

import com.github.rd.jlv.JlvActivator;
import com.github.rd.jlv.prefs.GeneralModel;
import com.github.rd.jlv.prefs.PreferenceEnum;
import com.github.rd.jlv.prefs.PresentationalModel;
import com.github.rd.jlv.prefs.StructuralModel;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		PreferenceManager preferenceManager = JlvActivator.getDefault().getPreferenceManager();
		preferenceManager.setDefault(PreferenceEnum.JLV_GENERAL_SETTINGS, GeneralModel.class);
		preferenceManager.setDefault(PreferenceEnum.LOG_LIST_STRUCTURAL_TABLE_SETTINGS, StructuralModel.class);
		preferenceManager.setDefault(PreferenceEnum.LOG_LIST_PRESENTATIONAL_TABLE_SETTINGS, PresentationalModel.class);
	}
}
