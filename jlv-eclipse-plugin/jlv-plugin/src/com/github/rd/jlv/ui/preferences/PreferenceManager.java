package com.github.rd.jlv.ui.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

import com.github.rd.jlv.pfers.Converter;
import com.github.rd.jlv.pfers.ConverterFactory;
import com.github.rd.jlv.pfers.GeneralModel;
import com.github.rd.jlv.pfers.PreferenceEnum;
import com.github.rd.jlv.pfers.PresentationalModel;
import com.github.rd.jlv.pfers.StructuralModel;

public final class PreferenceManager {

	private IPreferenceStore store;

	private Converter<GeneralModel> generalModelConverter;
	private Converter<StructuralModel> structuralModelConverter;
	private Converter<PresentationalModel> presentationalModelConverter;

	private GeneralModel generalModel;
	private StructuralModel structuralModel;
	private PresentationalModel presentationalModel;

	public PreferenceManager(IPreferenceStore store) {
		this.store = store;

		generalModelConverter = ConverterFactory.getConverter(GeneralModel.class);
		generalModel = generalModelConverter
				.jsonToModel(store.getString(PreferenceEnum.JLV_GENERAL_SETTINGS.getName()));

		structuralModelConverter = ConverterFactory.getConverter(StructuralModel.class);
		structuralModel = structuralModelConverter.jsonToModel(store
				.getString(PreferenceEnum.LOG_LIST_STRUCTURAL_TABLE_SETTINGS.getName()));

		presentationalModelConverter = ConverterFactory.getConverter(PresentationalModel.class);
		presentationalModel = presentationalModelConverter.jsonToModel(store
				.getString(PreferenceEnum.LOG_LIST_PRESENTATIONAL_TABLE_SETTINGS.getName()));
	}

	public GeneralModel getGeneralPrefs() {
		return generalModel;
	}

	public GeneralModel getDefaultGeneralPrefs() {
		return generalModelConverter.getDefaultModel();
	}

	public void storeGeneralPrefs(GeneralModel model) {
		String data = generalModelConverter.modelToJson(model);
		store.setValue(PreferenceEnum.JLV_GENERAL_SETTINGS.getName(), data);
	}

	public StructuralModel getStructuralPrefs() {
		return structuralModel;
	}

	public StructuralModel getDefaultStructuralPrefs() {
		return structuralModelConverter.getDefaultModel();
	}

	public void storeStructuralPrefs(StructuralModel model) {
		String data = structuralModelConverter.modelToJson(model);
		store.setValue(PreferenceEnum.LOG_LIST_STRUCTURAL_TABLE_SETTINGS.getName(), data);
	}

	public PresentationalModel getPresentationalPrefs() {
		return presentationalModel;
	}

	public PresentationalModel getDefaultPresentationalPrefs() {
		return presentationalModelConverter.getDefaultModel();
	}

	public void storePresentationalPrefs(PresentationalModel model) {
		String data = presentationalModelConverter.modelToJson(model);
		store.setValue(PreferenceEnum.LOG_LIST_PRESENTATIONAL_TABLE_SETTINGS.getName(), data);
	}
}
