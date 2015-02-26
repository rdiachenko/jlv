package com.github.rd.jlv.eclipse;

public enum ImageType {

	CHECKBOX_CHECKED_ICON("/icons/checkboxChecked.gif"),
	CHECKBOX_UNCHECKED_ICON("/icons/checkboxUnchecked.gif"),
	DEBUG_LEVEL_ICON("/icons/debug.png"),
	INFO_LEVEL_ICON("/icons/info.png"),
	WARN_LEVEL_ICON("/icons/warn.png"),
	ERROR_LEVEL_ICON("/icons/error.png"),
	FATAL_LEVEL_ICON("/icons/fatal.png");

	private String path;

	private ImageType(String path) {
		this.path = path;
	}

	public String path() {
		return path;
	}
}
