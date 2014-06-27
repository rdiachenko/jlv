package com.github.rd.jlv.ui.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;

import com.github.rd.jlv.ResourceManager;
import com.github.rd.jlv.pfers.Converter;
import com.github.rd.jlv.pfers.ConverterFactory;
import com.github.rd.jlv.pfers.GeneralModel;
import com.github.rd.jlv.pfers.PreferenceEnum;
import com.github.rd.jlv.pfers.PresentationalModel;
import com.github.rd.jlv.pfers.PresentationalModel.ModelItem.Rgb;
import com.github.rd.jlv.pfers.StructuralModel;

public final class PreferenceManager {

	private IPreferenceStore store;

	private GeneralModel generalModel;
	private StructuralModel structuralModel;
	private PresentationalModel presentationalModel;

	public PreferenceManager(IPreferenceStore store) {
		this.store = store;
		generalModel = getValue(PreferenceEnum.JLV_GENERAL_SETTINGS, GeneralModel.class);
		structuralModel = getValue(PreferenceEnum.LOG_LIST_STRUCTURAL_TABLE_SETTINGS, StructuralModel.class);
		presentationalModel = getValue(PreferenceEnum.LOG_LIST_PRESENTATIONAL_TABLE_SETTINGS, PresentationalModel.class);
	}

	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		if (listener != null) {
			store.addPropertyChangeListener(listener);
		}
	}

	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		if (listener != null) {
			store.removePropertyChangeListener(listener);
		}
	}

	public <T> T getDefault(PreferenceEnum preference, Class<T> modelType) {
		Converter<T> converter = ConverterFactory.getConverter(modelType);
		T model = converter.getDefaultModel();
		return model;
	}

	public <T> void setDefault(PreferenceEnum preference, Class<T> modelType) {
		Converter<T> converter = ConverterFactory.getConverter(modelType);
		T model = converter.getDefaultModel();
		store.setDefault(preference.getName(), converter.modelToJson(model));
	}

	public <T> T getValue(PreferenceEnum preference, Class<T> modelType) {
		T model = getExistingValue(preference, modelType);

		if (model == null) {
			Converter<T> converter = ConverterFactory.getConverter(modelType);
			model = converter.jsonToModel(store.getString(preference.getName()));
			update(preference, model);
		}
		return model;
	}

	public <T> void setValue(PreferenceEnum preference, Class<T> modelType, T model) {
		Converter<T> converter = ConverterFactory.getConverter(modelType);
		String data = converter.modelToJson(model);
		store.setValue(preference.getName(), data);
		update(preference, model);
	}

	// General preferences

	public boolean isServerAutoStart() {
		return generalModel.isAutoStart();
	}

	public boolean isQuickSearchVisible() {
		return generalModel.isQuickSearch();
	}

	public void storeQuickSearchState(boolean visible) {
		generalModel.setQuickSearch(visible);
		setValue(PreferenceEnum.JLV_GENERAL_SETTINGS, GeneralModel.class, generalModel);
	}

	public int getLogsBufferSize() {
		return generalModel.getBufferSize();
	}

	public int getServerPortNumber() {
		return generalModel.getPortNumber();
	}

	public int getRefreshingTime() {
		return generalModel.getRefreshingTime();
	}

	// Structural preferences

	public void storeColumnWidth(String columnName, int width) {
		for (StructuralModel.ModelItem item : structuralModel.getModelItems()) {
			if (columnName.equalsIgnoreCase(item.getName())) {
				item.setWidth(width);
			}
		}
		setValue(PreferenceEnum.LOG_LIST_STRUCTURAL_TABLE_SETTINGS, StructuralModel.class, structuralModel);
	}

	// Presentational preferences

	public boolean isLogLevelAsImage() {
		return presentationalModel.isLevelAsImage();
	}

	public int getFontSize() {
		return presentationalModel.getFontSize();
	}

	public Rgb getLogLevelRgb(String levelName) {
		return getLogLevelRgb(levelName, true);
	}

	public Rgb getLogLevelRgb(String levelName, boolean foreground) {
		for (PresentationalModel.ModelItem item : presentationalModel.getModelItems()) {
			if (levelName.equalsIgnoreCase(item.getLevelName())) {
				if (foreground) {
					return item.getForeground();
				} else {
					return item.getBackground();
				}
			}
		}

		if (foreground) {
			return ResourceManager.FOREGROUND;
		} else {
			return ResourceManager.BACKGROUND;
		}
	}

	private <T> void update(PreferenceEnum preference, T model) {
		switch (preference) {
		case JLV_GENERAL_SETTINGS:
			generalModel = (GeneralModel) model;
			break;
		case LOG_LIST_STRUCTURAL_TABLE_SETTINGS:
			structuralModel = (StructuralModel) model;
			break;
		case LOG_LIST_PRESENTATIONAL_TABLE_SETTINGS:
			presentationalModel = (PresentationalModel) model;
			break;
		default:
			throw new IllegalArgumentException("No such a preference found: " + preference.getName());
		}
	}

	private <T> T getExistingValue(PreferenceEnum preference, Class<T> modelType) {
		switch (preference) {
		case JLV_GENERAL_SETTINGS:
			return modelType.cast(generalModel);
		case LOG_LIST_STRUCTURAL_TABLE_SETTINGS:
			return modelType.cast(structuralModel);
		case LOG_LIST_PRESENTATIONAL_TABLE_SETTINGS:
			return modelType.cast(presentationalModel);
		default:
			throw new IllegalArgumentException("No such a preference found: " + preference.getName());
		}
	}
}
