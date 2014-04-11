package com.github.rd.jlv.ui.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.github.rd.jlv.ImageType;
import com.github.rd.jlv.ResourceManager;
import com.github.rd.jlv.log4j.LogConstants;
import com.github.rd.jlv.model.PresentationalModel;
import com.github.rd.jlv.model.StructuralModel;
import com.github.rd.jlv.model.converters.PresentationalModelConverter;
import com.github.rd.jlv.model.converters.StructuralModelConverter;

public final class PreferenceManager {

	public static final String SERVER_PORT_NUMBER = "jlv.server.port.number";
	public static final String SERVER_AUTO_START = "jlv.server.auto.start";

	public static final String QUICK_SEARCH_FIELD_VISIBLE = "jlv.loglistview.quick.search.field.visible";

	public static final String LOGS_BUFFER_SIZE = "jlv.loglistview.buffer.size";
	public static final String LOGS_REFRESHING_TIME = "jlv.loglistview.refreshing.time";

	public static final String STRUCTURAL_TABLE_SETTINGS = "jlv.loglistview.structural.settings";
	public static final String PRESENTATIONAL_TABLE_SETTINGS = "jlv.loglistview.presentational.settings";

	private IPreferenceStore store;
	private ResourceManager resourceManager;

	private StructuralModelConverter structuralModelConverter;
	private PresentationalModelConverter presentationalModelConverter;
	private PresentationalModel presentationalModel;

	private IPropertyChangeListener propertyChangeListener;

	public PreferenceManager(IPreferenceStore store, ResourceManager resourceManager) {
		this.store = store;
		this.resourceManager = resourceManager;
		structuralModelConverter = new StructuralModelConverter();
		presentationalModelConverter = new PresentationalModelConverter();
		presentationalModel = getPresentationalModel();
		propertyChangeListener = new PropertyChangeListener();
		addPropertyChangeListener(propertyChangeListener);
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

	public int getServerPortNumber() {
		return store.getInt(SERVER_PORT_NUMBER);
	}

	public boolean isServerAutoStart() {
		return store.getBoolean(SERVER_AUTO_START);
	}

	public void setQuickSearchFieldVisible(boolean isVisible) {
		store.setValue(QUICK_SEARCH_FIELD_VISIBLE, isVisible);
	}

	public boolean isQuickSearchFieldVisible() {
		return store.getBoolean(QUICK_SEARCH_FIELD_VISIBLE);
	}

	public int getLogsBufferSize() {
		return store.getInt(LOGS_BUFFER_SIZE);
	}

	public int getLogsRefreshingTime() {
		return store.getInt(LOGS_REFRESHING_TIME);
	}

	public StructuralModel getDefaultStructuralModel() {
		String data = store.getDefaultString(STRUCTURAL_TABLE_SETTINGS);
		return getStructuralModel(data);
	}

	public StructuralModel getStructuralModel() {
		String data = store.getString(STRUCTURAL_TABLE_SETTINGS);
		return getStructuralModel(data);
	}

	public StructuralModel getStructuralModel(String data) {
		return structuralModelConverter.getModel(data);
	}

	public void storeStructuralModel(String columnName, int width) {
		StructuralModel model = getStructuralModel();

		for (StructuralModel.ModelItem item : model.getModelItems()) {
			if (columnName.equalsIgnoreCase(item.getName())) {
				item.setWidth(width);
			}
		}
		storeStructuralModel(model);
	}

	public void storeStructuralModel(StructuralModel model) {
		String data = structuralModelConverter.modelAsJson(model);
		store.setValue(STRUCTURAL_TABLE_SETTINGS, data);
	}

	public PresentationalModel getDefaultPresentationalModel() {
		String data = store.getDefaultString(PRESENTATIONAL_TABLE_SETTINGS);
		return getPresentationalModel(data);
	}

	public PresentationalModel getPresentationalModel() {
		String data = store.getString(PRESENTATIONAL_TABLE_SETTINGS);
		return getPresentationalModel(data);
	}

	public PresentationalModel getPresentationalModel(String data) {
		return presentationalModelConverter.getModel(data);
	}

	public boolean isLevelAsImage() {
		return presentationalModel.isLevelAsImage();
	}

	public Font getFont(Display display) {
		if (display == null) {
			return null;
		} else {
			return resourceManager.getFont(display, presentationalModel.getFontSize());
		}
	}

	public Color getColor(String levelName, int colorType, Display display) {
		PresentationalModel.ModelItem modelItem = presentationalModel.getModelItemsMap().get(levelName);

		if (modelItem == null || display == null) {
			return null;
		} else {
			switch (colorType) {
			case SWT.BACKGROUND:
				return resourceManager.getColor(display, modelItem.getBackground());
			case SWT.FOREGROUND:
				return resourceManager.getColor(display, modelItem.getForeground());
			default:
				throw new IllegalArgumentException("No such color type: " + colorType);
			}
		}
	}

	public Image getLevelImage(String levelName) {
		switch (levelName) {
		case LogConstants.DEBUG_LEVEL_NAME:
			return resourceManager.getImage(ImageType.DEBUG_LEVEL_ICON);
		case LogConstants.INFO_LEVEL_NAME:
			return resourceManager.getImage(ImageType.INFO_LEVEL_ICON);
		case LogConstants.WARN_LEVEL_NAME:
			return resourceManager.getImage(ImageType.WARN_LEVEL_ICON);
		case LogConstants.ERROR_LEVEL_NAME:
			return resourceManager.getImage(ImageType.ERROR_LEVEL_ICON);
		case LogConstants.FATAL_LEVEL_NAME:
			return resourceManager.getImage(ImageType.FATAL_LEVEL_ICON);
		default:
			throw new IllegalArgumentException("No log level with such name: " + levelName);
		}
	}

	public void storePresentationalModel(PresentationalModel model) {
		String data = presentationalModelConverter.modelAsJson(model);
		store.setValue(PRESENTATIONAL_TABLE_SETTINGS, data);
	}

	public void dispose() {
		removePropertyChangeListener(propertyChangeListener);
	}

	private class PropertyChangeListener implements IPropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			if (PRESENTATIONAL_TABLE_SETTINGS.equals(event.getProperty())) {
				presentationalModel = getPresentationalModel();
			}
		}
	}
}
