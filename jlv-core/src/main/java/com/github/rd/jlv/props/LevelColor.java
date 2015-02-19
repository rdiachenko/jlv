package com.github.rd.jlv.props;

import com.google.common.base.Objects;

public class LevelColor {

	private final int red;
	private final int green;
	private final int blue;
	
	public LevelColor(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public int getRed() {
		return red;
	}

	public int getGreen() {
		return green;
	}

	public int getBlue() {
		return blue;
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.omitNullValues()
				.add("red", red)
				.add("green", green)
				.add("blue", blue)
				.toString();
	}
}
