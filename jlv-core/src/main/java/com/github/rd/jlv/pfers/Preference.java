package com.github.rd.jlv.pfers;

public enum Preference {

	SERVER_PORT_NUMBER("server.port.number"),
	SERVER_AUTO_START("server.auto.start"),
	QUICK_SEARCH_FIELD("quick.search.field.visible"),
	LOG_LIST_BUFFER_SIZE("loglist.buffer.size"),
	LOG_LIST_REFRESHING_TIME("loglist.refreshing.time"),
	LOG_LIST_STRUCTURAL_TABLE_SETTINGS("loglist.structural.table.settings"),
	LOG_LIST_PRESENTATIONAL_TABLE_SETTINGS("loglist.presentational.table.settings");

	private String name;

	private Preference(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
