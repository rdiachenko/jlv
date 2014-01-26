package com.github.rd.jlv.log4j.dao;

public final class DbConstants {

	// DB Log table fields
	public static final String CATEGORY_FIELD_NAME = "category";
	public static final String CLASS_FIELD_NAME = "class";
	public static final String DATE_FIELD_NAME = "date";
	public static final String FILE_FIELD_NAME = "file";
	public static final String LOCATION_INFO_FIELD_NAME = "locInfo";
	public static final String LINE_FIELD_NAME = "line";
	public static final String METHOD_FIELD_NAME = "method";
	public static final String LEVEL_FIELD_NAME = "level";
	public static final String MILLISECONDS_FIELD_NAME = "ms";
	public static final String THREAD_FIELD_NAME = "thread";
	public static final String MESSAGE_FIELD_NAME = "message";
	public static final String THROWABLE_FIELD_NAME = "throwable";
	public static final String NDC_FIELD_NAME = "ndc";
	public static final String MDC_FIELD_NAME = "mdc";

	private DbConstants() {
		throw new IllegalStateException("Private constructor's call on an util class");
	}
}
