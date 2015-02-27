package com.github.rd.jlv.eclipse.ui.preferences;


public final class PreferenceManager {

//	private IPreferenceStore store;
//
//	private GeneralModel generalModel;
//	private StructuralModel structuralModel;
//	private PresentationalModel presentationalModel;
//
//	private Map<PreferenceEnum, Converter> converters = new EnumMap<>(PreferenceEnum.class);
//
//	public PreferenceManager(IPreferenceStore store) {
//		this.store = store;
//		generalModel = (GeneralModel) initAndGetModel(PreferenceEnum.JLV_GENERAL_SETTINGS);
//		structuralModel = (StructuralModel) initAndGetModel(PreferenceEnum.LOG_LIST_STRUCTURAL_TABLE_SETTINGS);
//		presentationalModel = (PresentationalModel) initAndGetModel(PreferenceEnum.LOG_LIST_PRESENTATIONAL_TABLE_SETTINGS);
//	}
//
//	public void addPropertyChangeListener(IPropertyChangeListener listener) {
//		if (listener != null) {
//			store.addPropertyChangeListener(listener);
//		}
//	}
//
//	public void removePropertyChangeListener(IPropertyChangeListener listener) {
//		if (listener != null) {
//			store.removePropertyChangeListener(listener);
//		}
//	}
//
//	public void setValue(PreferenceEnum type, Model model) {
//		update(type, model);
//		Converter converter = converters.get(type);
//		String data = converter.modelToJson(model);
//		store.setValue(type.getName(), data);
//	}
//
//	// General preferences
//
//	public boolean isServerAutoStart() {
//		return generalModel.isAutoStart();
//	}
//
//	public boolean isQuickSearchVisible() {
//		return generalModel.isQuickSearch();
//	}
//
//	public void storeQuickSearchState(boolean visible) {
//		generalModel.setQuickSearch(visible);
//		setValue(PreferenceEnum.JLV_GENERAL_SETTINGS, generalModel);
//	}
//
//	public int getLogsBufferSize() {
//		return generalModel.getBufferSize();
//	}
//
//	public int getServerPortNumber() {
//		return generalModel.getPortNumber();
//	}
//
//	public int getRefreshingTime() {
//		return generalModel.getRefreshingTime();
//	}
//
//	// Structural preferences
//
//	public void storeColumnWidth(String columnName, int width) {
//		for (StructuralModel.ModelItem item : structuralModel.getModelItems()) {
//			if (columnName.equalsIgnoreCase(item.getName())) {
//				item.setWidth(width);
//			}
//		}
//		setValue(PreferenceEnum.LOG_LIST_STRUCTURAL_TABLE_SETTINGS, structuralModel);
//	}
//
//	public List<ModelItem> getStructuralItems() {
//		StructuralModel model = new StructuralModel(structuralModel);
//		return model.getModelItems();
//	}
//
//	// Presentational preferences
//
//	public boolean isLogLevelAsImage() {
//		return presentationalModel.isLevelAsImage();
//	}
//
//	public int getFontSize() {
//		return presentationalModel.getFontSize();
//	}
//
//	public Rgb getLogLevelRgb(String levelName) {
//		return getLogLevelRgb(levelName, true);
//	}
//
//	public Rgb getLogLevelRgb(String levelName, boolean foreground) {
//		for (PresentationalModel.ModelItem item : presentationalModel.getModelItems()) {
//			if (levelName.equalsIgnoreCase(item.getLevelName())) {
//				if (foreground) {
//					return item.getForeground();
//				} else {
//					return item.getBackground();
//				}
//			}
//		}
//
//		if (foreground) {
//			return ResourceManager.FOREGROUND;
//		} else {
//			return ResourceManager.BACKGROUND;
//		}
//	}
//
//	private Model initAndGetModel(PreferenceEnum type) {
//		Converter converter = Converter.get(type);
//		converters.put(type, converter);
//		Model model = converter.jsonToModel(store.getString(type.getName()));
//		return model;
//	}
//
//	private void update(PreferenceEnum type, Model model) {
//		switch (type) {
//		case JLV_GENERAL_SETTINGS:
//			generalModel = (GeneralModel) model;
//			break;
//		case LOG_LIST_STRUCTURAL_TABLE_SETTINGS:
//			structuralModel = (StructuralModel) model;
//			break;
//		case LOG_LIST_PRESENTATIONAL_TABLE_SETTINGS:
//			presentationalModel = (PresentationalModel) model;
//			break;
//		default:
//			throw new IllegalArgumentException("No such a preference type found: " + type.getName());
//		}
//	}
}