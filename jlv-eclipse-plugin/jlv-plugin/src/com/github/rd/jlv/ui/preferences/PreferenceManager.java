package com.github.rd.jlv.ui.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;

import com.github.rd.jlv.ResourceUtility;
import com.github.rd.jlv.pfers.Converter;
import com.github.rd.jlv.pfers.ConverterFactory;
import com.github.rd.jlv.pfers.GeneralModel;
import com.github.rd.jlv.pfers.PreferenceEnum;
import com.github.rd.jlv.pfers.PresentationalModel;
import com.github.rd.jlv.pfers.PresentationalModel.ModelItem.Rgb;
import com.github.rd.jlv.pfers.StructuralModel;

public final class PreferenceManager {

	private IPreferenceStore store;

	private Converter<GeneralModel> generalModelConverter;
	private Converter<StructuralModel> structuralModelConverter;
	private Converter<PresentationalModel> presentationalModelConverter;

	private GeneralModel generalModel;
	private StructuralModel structuralModel;
	private PresentationalModel presentationalModel;

	private DetailedPrefs detailedPrefs = new DetailedPrefs();

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

	public DetailedPrefs getDetailedPrefs() {
		return detailedPrefs;
	}

	public GeneralModel getGeneralPrefs() {
		return generalModel;
	}

	public GeneralModel getDefaultGeneralPrefs() {
		return generalModelConverter.getDefaultModel();
	}

	public void storeGeneralPrefs(GeneralModel model) {
		generalModel = model;
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
		structuralModel = model;
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
		presentationalModel = model;
		String data = presentationalModelConverter.modelToJson(model);
		store.setValue(PreferenceEnum.LOG_LIST_PRESENTATIONAL_TABLE_SETTINGS.getName(), data);
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

	public class DetailedPrefs {

		// General preferences

		public boolean isServerAutoStart() {
			return getGeneralPrefs().isAutoStart();
		}

		public boolean isQuickSearchVisible() {
			return getGeneralPrefs().isQuickSearch();
		}

		public void storeQuickSearchState(boolean visible) {
			GeneralModel model = getGeneralPrefs();
			model.setQuickSearch(visible);
			storeGeneralPrefs(model);
		}

		public int getLogsBufferSize() {
			return getGeneralPrefs().getBufferSize();
		}

		public int getServerPortNumber() {
			return getGeneralPrefs().getPortNumber();
		}

		public int getRefreshingTime() {
			return getGeneralPrefs().getRefreshingTime();
		}

		// Structural preferences

		public void storeColumnWidth(String columnName, int width) {
			StructuralModel model = getStructuralPrefs();

			for (StructuralModel.ModelItem item : model.getModelItems()) {
				if (columnName.equalsIgnoreCase(item.getName())) {
					item.setWidth(width);
				}
			}
			storeStructuralPrefs(model);
		}

		// Presentational preferences

		public boolean isLogLevelAsImage() {
			return getPresentationalPrefs().isLevelAsImage();
		}

		public int getFontSize() {
			return getPresentationalPrefs().getFontSize();
		}

		public Rgb getLogLevelRgb(String levelName) {
			return getLogLevelRgb(levelName, true);
		}

		public Rgb getLogLevelRgb(String levelName, boolean foreground) {
			PresentationalModel model = getPresentationalPrefs();

			for (PresentationalModel.ModelItem item : model.getModelItems()) {
				if (levelName.equalsIgnoreCase(item.getLevelName())) {
					if (foreground) {
						return item.getForeground();
					} else {
						return item.getBackground();
					}
				}
			}

			if (foreground) {
				return ResourceUtility.FOREGROUND;
			} else {
				return ResourceUtility.BACKGROUND;
			}
		}
	}
}
