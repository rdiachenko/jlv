package com.rdiachenko.jlv;

public enum ImageType {

	CHECKBOX_CHECKED("icons/checkboxChecked.gif"),
	CHECKBOX_UNCHECKED("icons/checkboxUnchecked.gif"),
	DEBUG("icons/debug.png"),
	INFO("icons/info.png"),
	WARN("icons/warn.png"),
	ERROR("icons/error.png"),
	FATAL("icons/fatal.png");

	private String path;

	private ImageType(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}
}
