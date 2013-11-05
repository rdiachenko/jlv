package com.github.incode.jlv.log4j.domain;

public enum LogFieldName {

	CATEGORY("category"),
	CLASS("class"),
	DATE("date"),
	FILE("file"),
	LOCATION_INFO("locInfo"),
	LINE("line"),
	METHOD("method"),
	LEVEL("level"),
	MILLISECONDS("ms"),
	THREAD("thread"),
	MESSAGE("message"),
	THROWABLE("throwable");

	private String name;

	private LogFieldName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
