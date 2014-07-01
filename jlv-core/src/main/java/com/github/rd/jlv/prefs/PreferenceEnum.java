package com.github.rd.jlv.prefs;

public enum PreferenceEnum {

	JLV_GENERAL_SETTINGS("jlv.general.settings"),
	LOG_LIST_STRUCTURAL_TABLE_SETTINGS("loglist.structural.table.settings"),
	LOG_LIST_PRESENTATIONAL_TABLE_SETTINGS("loglist.presentational.table.settings");

	private String name;

	private PreferenceEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
