package com.github.rd.jlv.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.github.rd.jlv.JlvActivator;
import com.github.rd.jlv.model.converters.PresentationalModelConverter;
import com.github.rd.jlv.model.converters.StructuralModelConverter;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = JlvActivator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceManager.SERVER_PORT_NUMBER, 4445);
		store.setDefault(PreferenceManager.SERVER_AUTO_START, true);

		store.setDefault(PreferenceManager.QUICK_SEARCH_FIELD_VISIBLE, true);

		store.setDefault(PreferenceManager.LOGS_BUFFER_SIZE, 1000);
		store.setDefault(PreferenceManager.LOGS_REFRESHING_TIME, 500);

		StructuralModelConverter structuralModelConverter = new StructuralModelConverter();
		store.setDefault(PreferenceManager.STRUCTURAL_TABLE_SETTINGS, structuralModelConverter.defaultModelAsJson());

		PresentationalModelConverter presentationalModelConverter = new PresentationalModelConverter();
		store.setDefault(PreferenceManager.PRESENTATIONAL_TABLE_SETTINGS,
				presentationalModelConverter.defaultModelAsJson());
	}
}
