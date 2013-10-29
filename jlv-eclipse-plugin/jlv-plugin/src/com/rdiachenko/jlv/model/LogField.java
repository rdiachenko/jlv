package com.rdiachenko.jlv.model;

import com.rdiachenko.jlv.log4j.domain.Log;

public enum LogField {

	LEVEL("Level"),
	CATEGORY("Category"),
	MESSAGE("Message"),
	LINE("Line"),
	DATE("Date"),
	THROWABLE("Throwable");

	private String name;

	private LogField(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getValue(Log log) {
		String value = "";

		switch (this) {
		case LEVEL:
			value = log.getLevel();
			break;
		case CATEGORY:
			value = log.getCategoryName();
			break;
		case MESSAGE:
			value = log.getMessage();
			break;
		case LINE:
			value = log.getLineNumber();
			break;
		case DATE:
			value = log.getDate();
			break;
		case THROWABLE:
			value = log.getThrowable();
			break;
		default:
			throw new IllegalStateException(
					"Only [LEVEL,CATEGORY,MESSAGE,LINE,DATE,THROWABLE] log fields are available. "
							+ "Current value: " + this);
		}
		return value;
	}

	public static LogField getLogFieldByName(String name) {
		switch (name) {
		case "Level":
		case "level":
			return LogField.LEVEL;
		case "Category":
		case "category":
			return LogField.CATEGORY;
		case "Message":
		case "message":
			return LogField.MESSAGE;
		case "Line":
		case "line":
			return LogField.LINE;
		case "Date":
		case "date":
			return LogField.DATE;
		case "Throwable":
		case "throwable":
			return LogField.THROWABLE;
		default:
			throw new IllegalArgumentException("No log field with such name: " + name
					+ ". Available index range is [Level, Category, Message, Line, Date, Throwable]");
		}
	}
}
