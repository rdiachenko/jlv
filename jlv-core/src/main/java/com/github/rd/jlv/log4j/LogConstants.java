package com.github.rd.jlv.log4j;

public final class LogConstants {

	// Human readable log field names
	public static final String LEVEL_FIELD_NAME = "Level";
	public static final String CATEGORY_FIELD_NAME = "Category";
	public static final String MESSAGE_FIELD_NAME = "Message";
	public static final String LINE_FIELD_NAME = "Line";
	public static final String DATE_FIELD_NAME = "Date";
	public static final String THROWABLE_FIELD_NAME = "Throwable";
	public static final String CLASS_FIELD_NAME = "Class";
	public static final String FILE_FIELD_NAME = "File";
	public static final String LOCATION_INFO_FIELD_NAME = "Location Info";
	public static final String METHOD_FIELD_NAME = "Method";
	public static final String MILLISECONDS_FIELD_NAME = "Milliseconds";
	public static final String THREAD_FIELD_NAME = "Thread";

	public static final String[] LOG_FIELD_NAMES = {
			LEVEL_FIELD_NAME,
			CATEGORY_FIELD_NAME,
			MESSAGE_FIELD_NAME,
			LINE_FIELD_NAME,
			DATE_FIELD_NAME,
			THROWABLE_FIELD_NAME,
			CLASS_FIELD_NAME,
			FILE_FIELD_NAME,
			LOCATION_INFO_FIELD_NAME,
			METHOD_FIELD_NAME,
			MILLISECONDS_FIELD_NAME,
			THREAD_FIELD_NAME
	};

	private LogConstants() {
		throw new IllegalStateException("Private constructor's call on an util class");
	}
}
