package com.github.incode.jlv.ui.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;

import com.github.incode.jlv.JlvActivator;
import com.github.incode.jlv.ui.preferences.additional.LogsTableStructureItem;
import com.github.incode.jlv.ui.preferences.additional.LogsTableStructureManager;

public final class PreferenceManager {

	public static final String SERVER_PORT_NUMBER = "jlv.server.port.number";
	public static final String SERVER_AUTO_START = "jlv.server.auto.start";

	public static final String QUICK_SEARCH_FIELD_STATUS = "jlv.loglistview.quick.search.field.visible";

	public static final String LOGS_BUFFER_SIZE = "jlv.loglistview.buffer.size";
	public static final String LOGS_REFRESHING_TIME = "jlv.loglistview.refreshing.time";

	public static final String LOGS_TABLE_STRUCTURE_SETTINGS = "jlv.loglistview.table.structure";
	public static final String LOGS_TABLE_PRESENTATION_SETTINGS = "jlv.loglistview.table.presentation";
	public static final String IMAGE_INSTEAD_OF_TEXT_LEVEL_STATE = "jlv.loglistview.table.presentation.level.image";
	public static final String LOGS_FONT_SIZE = "jlv.loglistview.table.presentation.font.size";

	private static final IPreferenceStore STORE = JlvActivator.getDefault().getPreferenceStore();
	private static LogsTableStructureManager logsTableStructureManager;

	static {
		logsTableStructureManager = new LogsTableStructureManager(STORE, LOGS_TABLE_STRUCTURE_SETTINGS);
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

	public static boolean getServerAutoStart() {
		return STORE.getBoolean(SERVER_AUTO_START);
	}

	public static void setQuickSearchFieldStatus(boolean isVisible) {
		STORE.setValue(QUICK_SEARCH_FIELD_STATUS, isVisible);
	}

	public static boolean getQuickSearchFieldStatus() {
		return STORE.getBoolean(QUICK_SEARCH_FIELD_STATUS);
	}

	public static int getLogsBufferSize() {
		return STORE.getInt(LOGS_BUFFER_SIZE);
	}

	public static int getLogsRefreshingTime() {
		return STORE.getInt(LOGS_REFRESHING_TIME);
	}

	public static LogsTableStructureItem[] getLogsTableStructure() {
		return logsTableStructureManager.loadStructure();
	}

	public static LogsTableStructureItem[] getLogsTableStructure(String structure) {
		return logsTableStructureManager.loadStructure(structure);
	}

	public static int getLogsFontSize() {
		return STORE.getInt(LOGS_FONT_SIZE);
	}

	public static boolean getImageInsteadOfTextLevelState() {
		return STORE.getBoolean(IMAGE_INSTEAD_OF_TEXT_LEVEL_STATE);
	}
}
