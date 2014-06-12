package com.github.rd.jlv.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.github.rd.jlv.JlvActivator;
import com.github.rd.jlv.pfers.Converter;
import com.github.rd.jlv.pfers.ConverterFactory;
import com.github.rd.jlv.pfers.GeneralModel;
import com.github.rd.jlv.pfers.PreferenceEnum;
import com.github.rd.jlv.pfers.PresentationalModel;
import com.github.rd.jlv.pfers.StructuralModel;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = JlvActivator.getDefault().getPreferenceStore();
		setDefault(store, PreferenceEnum.JLV_GENERAL_SETTINGS.getName(), GeneralModel.class);
		setDefault(store, PreferenceEnum.LOG_LIST_STRUCTURAL_TABLE_SETTINGS.getName(), StructuralModel.class);
		setDefault(store, PreferenceEnum.LOG_LIST_PRESENTATIONAL_TABLE_SETTINGS.getName(), PresentationalModel.class);
	}

	private <T> void setDefault(IPreferenceStore store, String preferenceName, Class<T> modelType) {
		Converter<T> converter = ConverterFactory.getConverter(modelType);
		T model = converter.getDefaultModel();
		store.setDefault(preferenceName, converter.modelToJson(model));
	}
}
