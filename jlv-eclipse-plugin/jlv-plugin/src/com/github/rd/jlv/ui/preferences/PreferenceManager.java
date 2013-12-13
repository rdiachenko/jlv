package com.github.rd.jlv.ui.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.RGB;

import com.github.rd.jlv.ui.LogLevel;
import com.github.rd.jlv.ui.preferences.additional.LogsDisplayModel;
import com.github.rd.jlv.ui.preferences.additional.LogsDisplayPreferenceManager;
import com.github.rd.jlv.ui.preferences.additional.LogsTableStructureItem;
import com.github.rd.jlv.ui.preferences.additional.LogsTableStructurePreferenceManager;

public final class PreferenceManager {

	public static final String SERVER_PORT_NUMBER = "jlv.server.port.number";
	public static final String SERVER_AUTO_START = "jlv.server.auto.start";

	public static final String QUICK_SEARCH_FIELD_VISIBLE = "jlv.loglistview.quick.search.field.visible";

	public static final String LOGS_BUFFER_SIZE = "jlv.loglistview.buffer.size";
	public static final String LOGS_REFRESHING_TIME = "jlv.loglistview.refreshing.time";

	public static final String LOGS_TABLE_STRUCTURE_SETTINGS = "jlv.loglistview.table.structure";
	public static final String LOGS_TABLE_PRESENTATION_SETTINGS = "jlv.loglistview.table.presentation";

	private IPreferenceStore store;

	private LogsTableStructurePreferenceManager logsTableStructurePreferenceManager;

	private LogsDisplayPreferenceManager logsDisplayPreferenceManager;
	private LogsDisplayModel logsDisplayModel;

	private IPropertyChangeListener propertyChangeListener;

	public PreferenceManager(IPreferenceStore store) {
		this.store = store;
		logsTableStructurePreferenceManager = new LogsTableStructurePreferenceManager(this.store,
				LOGS_TABLE_STRUCTURE_SETTINGS);
		logsDisplayPreferenceManager = new LogsDisplayPreferenceManager(this.store, LOGS_TABLE_PRESENTATION_SETTINGS);
		logsDisplayModel = logsDisplayPreferenceManager.loadModel();

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

	public LogsTableStructureItem[] getLogsTableStructure() {
		return logsTableStructurePreferenceManager.loadStructure();
	}

	public LogsTableStructureItem[] getLogsTableStructure(String structure) {
		return logsTableStructurePreferenceManager.loadStructure(structure);
	}

	public boolean isLevelImageSubstitutesText() {
		return logsDisplayModel.isLevelImageSubstitutesText();
	}

	public int getLogsFontSize() {
		return logsDisplayModel.getFontSize();
	}

	public RGB getLogsForeground(LogLevel logLevel) {
		return logsDisplayModel.getForegroundByLevel(logLevel);
	}

	public RGB getLogsBackground(LogLevel logLevel) {
		return logsDisplayModel.getBackgroundByLevel(logLevel);
	}

	public void dispose() {
		removePropertyChangeListener(propertyChangeListener);
	}

	private class PropertyChangeListener implements IPropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			if (LOGS_TABLE_PRESENTATION_SETTINGS.equals(event.getProperty())) {
				logsDisplayModel = logsDisplayPreferenceManager.loadModel();
			}
		}
	}
}
