package com.github.rd.jlv.props;

import com.google.common.base.MoreObjects;
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
	public int hashCode() {
		return Objects.hashCode(name, visible, width);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null || obj.getClass() != getClass()) {
			return false;
		} else {
			LoglistColumn that = (LoglistColumn) obj;
			return Objects.equal(name, that.name)
					&& Objects.equal(visible, that.visible)
					&& Objects.equal(width, that.width);
		}
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.omitNullValues()
				.add("name", name)
				.add("visible", visible)
				.add("width", width)
				.toString();
	}
}
