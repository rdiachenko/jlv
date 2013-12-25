package com.github.rd.jlv.ui.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;

import com.github.rd.jlv.ui.preferences.additional.PresentationalPreferenceManager;
import com.github.rd.jlv.ui.preferences.additional.PresentationalPreferenceModel;
import com.github.rd.jlv.ui.preferences.additional.StructuralPreferenceManager;
import com.github.rd.jlv.ui.preferences.additional.StructuralPreferenceModel;

public final class PreferenceManager {

	public static final String SERVER_PORT_NUMBER = "jlv.server.port.number";
	public static final String SERVER_AUTO_START = "jlv.server.auto.start";

	public static final String QUICK_SEARCH_FIELD_VISIBLE = "jlv.loglistview.quick.search.field.visible";

	public static final String LOGS_BUFFER_SIZE = "jlv.loglistview.buffer.size";
	public static final String LOGS_REFRESHING_TIME = "jlv.loglistview.refreshing.time";

	public static final String STRUCTURAL_TABLE_SETTINGS = "jlv.loglistview.structural.settings";
	public static final String PRESENTATIONAL_TABLE_SETTINGS = "jlv.loglistview.presentational.settings";

	private IPreferenceStore store;

	private StructuralPreferenceManager structuralPreferenceManager;

	private PresentationalPreferenceManager presentationalPreferenceManager;
	private PresentationalPreferenceModel presentationalPreferenceModel;

	private IPropertyChangeListener propertyChangeListener;

	public PreferenceManager(IPreferenceStore store) {
		this.store = store;
		structuralPreferenceManager = new StructuralPreferenceManager(this.store, STRUCTURAL_TABLE_SETTINGS);
		presentationalPreferenceManager = new PresentationalPreferenceManager(this.store, PRESENTATIONAL_TABLE_SETTINGS);
		presentationalPreferenceModel = presentationalPreferenceManager.loadModel();
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

	public StructuralPreferenceModel[] getStructuralPreferenceModel() {
		return structuralPreferenceManager.loadStructure();
	}

	public StructuralPreferenceModel[] getStructuralPreferenceModel(String structure) {
		return structuralPreferenceManager.loadStructure(structure);
	}

	public void setStructuralPreferenceModel(String columnName, int width) {
		StructuralPreferenceModel[] structure = getStructuralPreferenceModel();

		for (StructuralPreferenceModel model : structure) {
			if (columnName.equals(model.getName())) {
				model.setWidth(width);
				structuralPreferenceManager.storeTableStructure(structure);
				break;
			}
		}
	}

	public boolean isLevelAsImage() {
		return presentationalPreferenceModel.isLevelAsImage();
	}

	public int getFontSize() {
		return presentationalPreferenceModel.getFontSize();
	}

	public RGB getForeground(String levelName) {
		return presentationalPreferenceModel.getForeground(levelName);
	}

	public RGB getBackground(String levelName) {
		return presentationalPreferenceModel.getBackground(levelName);
	}

	public Image getLevelImage(String levelName) {
		return presentationalPreferenceModel.getImage(levelName);
	}

	public void dispose() {
		removePropertyChangeListener(propertyChangeListener);
	}

	private class PropertyChangeListener implements IPropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			if (PRESENTATIONAL_TABLE_SETTINGS.equals(event.getProperty())) {
				presentationalPreferenceModel = presentationalPreferenceManager.loadModel();
			}
		}
	}
}
