package com.rdiachenko.jlv.ui.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

import com.google.common.base.Strings;
import com.rdiachenko.jlv.JlvActivator;
import com.rdiachenko.jlv.ui.preferences.additional.LogsTableStructureItem;

public final class PreferenceManager {

	public static final String SERVER_PORT_NUMBER = "jlv.server.port.number";
	public static final String SERVER_AUTO_START = "jlv.server.auto.start";

	public static final String QUICK_SEARCH_FIELD_STATUS = "jlv.loglistview.quick.search.field.visible";

	public static final String LOGS_BUFFER_SIZE = "jlv.loglistview.buffer.size";
	public static final String LOGS_REFRESHING_TIME = "jlv.loglistview.refreshing.time";

	public static final String LOGS_TABLE_STRUCTURE_SETTINGS = "jlv.loglistview.table.structure";
	public static final String LOGS_TABLE_PRESENTATION_SETTINGS = "jlv.loglistview.table.presentation";

	private static final IPreferenceStore STORE = JlvActivator.getDefault().getPreferenceStore();

	private PreferenceManager() {
		throw new IllegalStateException("This is an util class. The object should not be created.");
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
		return LogsTableStructureLoader.loadStructure();
	}

	public static LogsTableStructureItem[] getLogsTableDefaultStructure() {
		return LogsTableStructureLoader.loadDefaultStructure();
	}

	public static void setLogsTableStructure(LogsTableStructureItem[] structure) {
		LogsTableStructureLoader.storeStructure(structure);
	}

	private static class LogsTableStructureLoader {

		private static final String COLUMN_SEPARATOR = ":";
		private static final String SEMICOLUMN_SEPARATOR = ";";

		public static LogsTableStructureItem[] loadStructure() {
			String prefs = STORE.getString(LOGS_TABLE_STRUCTURE_SETTINGS);

			if (Strings.isNullOrEmpty(prefs)) {
				return loadDefaultStructure();
			} else {
				return stringToStructure(prefs);
			}
		}

		public static LogsTableStructureItem[] loadDefaultStructure() {
			String prefs = STORE.getDefaultString(LOGS_TABLE_STRUCTURE_SETTINGS);
			return stringToStructure(prefs);
		}

		public static void storeStructure(LogsTableStructureItem[] structure) {
			String prefs = structureToString(structure);
			STORE.setValue(LOGS_TABLE_STRUCTURE_SETTINGS, prefs);
		}

		private static LogsTableStructureItem[] stringToStructure(String prefs) {
			String[] structureItems = prefs.split(SEMICOLUMN_SEPARATOR);
			LogsTableStructureItem[] structure = new LogsTableStructureItem[structureItems.length];

			for (int i = 0; i < structure.length; i++) {
				String[] structureItem = structureItems[i].split(COLUMN_SEPARATOR);
				String name = structureItem[0];
				int width = Integer.valueOf(structureItem[1]);
				boolean display = Boolean.valueOf(structureItem[2]);
				structure[i] = new LogsTableStructureItem(name, width, display);
			}
			return structure;
		}

		private static String structureToString(LogsTableStructureItem[] structure) {
			StringBuilder builder = new StringBuilder();

			for (LogsTableStructureItem structureItem : structure) {
				builder.append(structureItem.getName()).append(COLUMN_SEPARATOR)
						.append(structureItem.getWidth()).append(COLUMN_SEPARATOR)
						.append(structureItem.isDisplay()).append(SEMICOLUMN_SEPARATOR);
			}
			return builder.toString();
		}
	}
}
