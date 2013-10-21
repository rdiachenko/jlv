package com.rdiachenko.jlv;

public enum ImageType {

	CHECKBOX_CHECKED("icons/checkboxChecked.gif"),
	CHECKBOX_UNCHECKED("icons/checkboxUnchecked.gif");

	private String path;

	private ImageType(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}
}
