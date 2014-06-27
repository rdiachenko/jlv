package com.github.rd.jlv.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;

import com.github.rd.jlv.JlvActivator;
import com.github.rd.jlv.pfers.GeneralModel;
import com.github.rd.jlv.pfers.PreferenceEnum;
import com.github.rd.jlv.pfers.PresentationalModel;
import com.github.rd.jlv.pfers.StructuralModel;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		PreferenceManager preferenceManager = JlvActivator.getDefault().getPreferenceManager();
		preferenceManager.setDefault(PreferenceEnum.JLV_GENERAL_SETTINGS, GeneralModel.class);
		preferenceManager.setDefault(PreferenceEnum.LOG_LIST_STRUCTURAL_TABLE_SETTINGS, StructuralModel.class);
		preferenceManager.setDefault(PreferenceEnum.LOG_LIST_PRESENTATIONAL_TABLE_SETTINGS, PresentationalModel.class);
	}
}
