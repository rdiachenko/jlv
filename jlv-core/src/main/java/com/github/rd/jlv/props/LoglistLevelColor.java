package com.github.rd.jlv.props;

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
	public String toString() {
		return Objects.toStringHelper(this)
				.omitNullValues()
				.add("name", name)
				.add("foreground", foreground)
				.add("background", background)
				.toString();
	}
}
