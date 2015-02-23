package com.github.rd.jlv.props;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class LoglistLevelColor {

	private final String name;
	private final LevelColor foreground;
	private final LevelColor background;

	public LoglistLevelColor(String name, LevelColor foreground, LevelColor background) {
		this.name = name;
		this.foreground = foreground;
		this.background = background;
	}

	public String getName() {
		return name;
	}

	public LevelColor getForeground() {
		return foreground;
	}

	public LevelColor getBackground() {
		return background;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(name, foreground, background);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null || obj.getClass() != getClass()) {
			return false;
		} else {
			LoglistLevelColor that = (LoglistLevelColor) obj;
			return Objects.equal(name, that.name)
					&& Objects.equal(foreground, that.foreground)
					&& Objects.equal(background, that.background);
		}
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.omitNullValues()
				.add("name", name)
				.add("foreground", foreground)
				.add("background", background)
				.toString();
	}
}
