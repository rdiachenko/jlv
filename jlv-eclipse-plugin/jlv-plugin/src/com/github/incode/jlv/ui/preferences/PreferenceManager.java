package com.github.incode.jlv.ui.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;

import com.github.incode.jlv.JlvActivator;
import com.github.incode.jlv.ui.preferences.additional.LogsTableStructureItem;
import com.github.incode.jlv.ui.preferences.additional.LogsTableStructurePreferenceManager;

public final class PreferenceManager {

	public static final String SERVER_PORT_NUMBER = "jlv.server.port.number";
	public static final String SERVER_AUTO_START = "jlv.server.auto.start";

	public static final String QUICK_SEARCH_FIELD_VISIBLE = "jlv.loglistview.quick.search.field.visible";

	public static final String LOGS_BUFFER_SIZE = "jlv.loglistview.buffer.size";
	public static final String LOGS_REFRESHING_TIME = "jlv.loglistview.refreshing.time";

	public static final String LOGS_TABLE_STRUCTURE_SETTINGS = "jlv.loglistview.table.structure";
	public static final String LOGS_TABLE_PRESENTATION_SETTINGS = "jlv.loglistview.table.presentation";

	private static final IPreferenceStore STORE = JlvActivator.getDefault().getPreferenceStore();
	private static LogsTableStructurePreferenceManager logsTableStructurePreferenceManager;
//	private static LogsDisplayPreferenceManager logsDisplayPreferenceManager;

	static {
		logsTableStructurePreferenceManager = new LogsTableStructurePreferenceManager(STORE,
				LOGS_TABLE_STRUCTURE_SETTINGS);
//		logsDisplayPreferenceManager = new LogsDisplayPreferenceManager(STORE, LOGS_TABLE_PRESENTATION_SETTINGS);
	}

	private PreferenceManager() {
		throw new IllegalStateException("This is an util class. The object should not be created.");
	}

	public static void addPropertyChangeListener(IPropertyChangeListener listener) {
		STORE.addPropertyChangeListener(listener);
	}

	public static void removePropertyChangeListener(IPropertyChangeListener listener) {
		STORE.removePropertyChangeListener(listener);
	}

	public static int getServerPortNumber() {
		return STORE.getInt(SERVER_PORT_NUMBER);
	}

	public static boolean isServerAutoStart() {
		return STORE.getBoolean(SERVER_AUTO_START);
	}

	public static void setQuickSearchFieldVisible(boolean isVisible) {
		STORE.setValue(QUICK_SEARCH_FIELD_VISIBLE, isVisible);
	}

	public static boolean isQuickSearchFieldVisible() {
		return STORE.getBoolean(QUICK_SEARCH_FIELD_VISIBLE);
	}

	public static int getLogsBufferSize() {
		return STORE.getInt(LOGS_BUFFER_SIZE);
	}

	public static int getLogsRefreshingTime() {
		return STORE.getInt(LOGS_REFRESHING_TIME);
	}

	public static LogsTableStructureItem[] getLogsTableStructure() {
		return logsTableStructurePreferenceManager.loadStructure();
	}

	public static LogsTableStructureItem[] getLogsTableStructure(String structure) {
		return logsTableStructurePreferenceManager.loadStructure(structure);
	}

//	public static int getLogsFontSize() {
//		return STORE.getInt(LOGS_FONT_SIZE);
//	}
//
//	public static boolean isLevelImageSubstitutesText() {
//		return STORE.getBoolean(IMAGE_INSTEAD_OF_TEXT_LEVEL_STATE);
//	}
}
