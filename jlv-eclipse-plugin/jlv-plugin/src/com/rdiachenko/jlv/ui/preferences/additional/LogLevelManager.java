package com.rdiachenko.jlv.ui.preferences.additional;

import org.eclipse.jface.preference.IPreferenceStore;

import com.google.common.base.Strings;

public class LogLevelManager {

	private static final String COLUMN_SEPARATOR = ":";
	private static final String SEMICOLUMN_SEPARATOR = ";";

	private IPreferenceStore store;
	private String preferenceName;

	public LogLevelManager(IPreferenceStore store, String preferenceName) {
		this.store = store;
		this.preferenceName = preferenceName;
	}

	public LogsTableStructureItem[] loadStructure() {
		String prefs = store.getString(preferenceName);

		if (Strings.isNullOrEmpty(prefs)) {
			return loadDefaultStructure();
		} else {
			return stringToStructure(prefs);
		}
	}

	public LogsTableStructureItem[] loadStructure(String structure) {
		return stringToStructure(structure);
	}

	public LogsTableStructureItem[] loadDefaultStructure() {
		String prefs = store.getDefaultString(preferenceName);
		return stringToStructure(prefs);
	}

	public void storeTableStructure(LogsTableStructureItem[] structure) {
		String prefs = structureToString(structure);
		store.setValue(preferenceName, prefs);
	}

	private LogsTableStructureItem[] stringToStructure(String prefs) {
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

	private String structureToString(LogsTableStructureItem[] structure) {
		StringBuilder builder = new StringBuilder();

		for (LogsTableStructureItem structureItem : structure) {
			builder.append(structureItem.getName()).append(COLUMN_SEPARATOR)
					.append(structureItem.getWidth()).append(COLUMN_SEPARATOR)
					.append(structureItem.isDisplay()).append(SEMICOLUMN_SEPARATOR);
		}
		return builder.toString();
	}
}
