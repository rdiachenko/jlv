package com.github.rd.jlv.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.github.rd.jlv.JlvActivator;
import com.github.rd.jlv.prefs.Converter;
import com.github.rd.jlv.prefs.ConverterFactory;
import com.github.rd.jlv.prefs.GeneralModel;
import com.github.rd.jlv.prefs.PreferenceEnum;
import com.github.rd.jlv.prefs.PresentationalModel;
import com.github.rd.jlv.prefs.StructuralModel;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		setDefault(PreferenceEnum.JLV_GENERAL_SETTINGS, GeneralModel.class);
		setDefault(PreferenceEnum.LOG_LIST_STRUCTURAL_TABLE_SETTINGS, StructuralModel.class);
		setDefault(PreferenceEnum.LOG_LIST_PRESENTATIONAL_TABLE_SETTINGS, PresentationalModel.class);
	}

	private <T> void setDefault(PreferenceEnum preference, Class<T> modelType) {
		Converter<T> converter = ConverterFactory.getConverter(modelType);
		T model = converter.getDefaultModel();
		IPreferenceStore store = JlvActivator.getDefault().getPreferenceStore();
		store.setDefault(preference.getName(), converter.modelToJson(model));
	}
}
