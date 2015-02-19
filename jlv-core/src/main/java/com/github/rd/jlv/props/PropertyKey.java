package com.github.rd.jlv.props;

import com.github.rd.jlv.LogConstants;

public enum PropertyKey {

	SERVER_PORT_KEY("jlv.server.port", "4445"),
	SERVER_AUTOSTART_KEY("jlv.server.autostart", "true"),
	LOGLIST_BUFFER_SIZE_KEY("jlv.loglist.buffersize", "10000"),
	LOGLIST_REFRESH_TIME_KEY("jlv.loglist.refreshtime", "500"),
	LOGLIST_QUICK_SEARCH_KEY("jlv.loglist.quicksearch", "true"),
	LOGLIST_FONT_SIZE_KEY("jlv.loglist.fontsize", "11"),
	LOGLIST_LEVEL_IMAGE_KEY("jlv.loglist.levelimage", "true"),

	LOGLIST_LEVEL_COLOR_KEY("jlv.loglist.levelcolor",
			LogConstants.DEBUG_LEVEL_NAME + ":0-0-0:255-255-255;"
					+ LogConstants.INFO_LEVEL_NAME + ":0-255-0:255-255-255;"
					+ LogConstants.WARN_LEVEL_NAME + ":0-128-0:255-255-255;"
					+ LogConstants.ERROR_LEVEL_NAME + ":255-0-0:255-255-255;"
					+ LogConstants.FATAL_LEVEL_NAME + ":165-42-42:255-255-255;"),

	LOGLIST_COLUMN_KEY("jlv.loglist.column",
			LogConstants.LEVEL_FIELD_NAME + ":true:55;"
					+ LogConstants.CATEGORY_FIELD_NAME + ":true:100;"
					+ LogConstants.MESSAGE_FIELD_NAME + ":true:100;"
					+ LogConstants.LINE_FIELD_NAME + ":true:100;"
					+ LogConstants.DATE_FIELD_NAME + ":true:100;"
					+ LogConstants.THROWABLE_FIELD_NAME + ":true:100;");

	private String keyName;
	private String defaultValue;

	private PropertyKey(String keyName, String defaultValue) {
		this.keyName = keyName;
		this.defaultValue = defaultValue;
	}

	public String keyName() {
		return keyName;
	}

	public String defaultValue() {
		return defaultValue;
	}
}
