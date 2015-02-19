package com.github.rd.jlv.props;

import com.google.common.base.Objects;

public class LoglistColumn {

	private final String name;
	private final boolean visible;
	private final int width;
	
	public LoglistColumn(String name, boolean visible, int width) {
		this.name = name;
		this.visible = visible;
		this.width = width;
	}

	public String getName() {
		return name;
	}

	public boolean isVisible() {
		return visible;
	}

	public int getWidth() {
		return width;
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.omitNullValues()
				.add("name", name)
				.add("visible", visible)
				.add("width", width)
				.toString();
	}
}
