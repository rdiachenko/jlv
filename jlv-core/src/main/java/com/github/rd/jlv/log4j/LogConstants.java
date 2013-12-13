package com.github.rd.jlv.log4j;

import java.util.HashMap;
import java.util.Map;

public final class LogConstants {

	// DB Log table fields
	public static final String CATEGORY = "category";
	public static final String CLASS = "class";
	public static final String DATE = "date";
	public static final String FILE = "file";
	public static final String LOCATION_INFO = "locInfo";
	public static final String LINE = "line";
	public static final String METHOD = "method";
	public static final String LEVEL = "level";
	public static final String MILLISECONDS = "ms";
	public static final String THREAD = "thread";
	public static final String MESSAGE = "message";
	public static final String THROWABLE = "throwable";

	// Log field ids which should be the same as getters from com.github.rd.jlv.log4j.domain.Log
	public static final String LEVEL_ID = "level";
	public static final String CATEGORY_ID = "categoryName";
	public static final String MESSAGE_ID = "message";
	public static final String LINE_ID = "lineNumber";
	public static final String DATE_ID = "date";
	public static final String THROWABLE_ID = "throwable";

	public static final String[] LOG_FIELD_IDS = { LEVEL_ID, CATEGORY_ID, MESSAGE_ID, LEVEL_ID, DATE_ID, THROWABLE_ID };

	// Human readable log field names
	public static final String LEVEL_FIELD_NAME = "Level";
	public static final String CATEGORY_FIELD_NAME = "Category";
	public static final String MESSAGE_FIELD_NAME = "Message";
	public static final String LINE_FIELD_NAME = "Line";
	public static final String DATE_FIELD_NAME = "Date";
	public static final String THROWABLE_FIELD_NAME = "Throwable";

	public static final String[] LOG_FIELD_NAMES = { LEVEL_FIELD_NAME, CATEGORY_FIELD_NAME, MESSAGE_FIELD_NAME,
			LINE_FIELD_NAME, DATE_FIELD_NAME, THROWABLE_FIELD_NAME };

	public static final Map<String, String> ID_TO_FIELD_NAME_MAP = new HashMap<>();

	static {
		ID_TO_FIELD_NAME_MAP.put(LEVEL_ID, LEVEL_FIELD_NAME);
		ID_TO_FIELD_NAME_MAP.put(CATEGORY_ID, CATEGORY_FIELD_NAME);
		ID_TO_FIELD_NAME_MAP.put(MESSAGE_ID, MESSAGE_FIELD_NAME);
		ID_TO_FIELD_NAME_MAP.put(LINE_ID, LINE_FIELD_NAME);
		ID_TO_FIELD_NAME_MAP.put(DATE_ID, DATE_FIELD_NAME);
	}

	private LogConstants() {
		throw new IllegalStateException("Private constructor's call on an util class");
	}
}
