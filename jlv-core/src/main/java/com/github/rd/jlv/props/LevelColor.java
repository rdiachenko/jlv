package com.github.rd.jlv.props;

import com.google.common.base.MoreObjects;
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
	public int hashCode() {
		return Objects.hashCode(red, green, blue);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null || obj.getClass() != getClass()) {
			return false;
		} else {
			LevelColor that = (LevelColor) obj;
			return Objects.equal(red, that.red)
					&& Objects.equal(green, that.green)
					&& Objects.equal(blue, that.blue);
		}
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.omitNullValues()
				.add("red", red)
				.add("green", green)
				.add("blue", blue)
				.toString();
	}
}
