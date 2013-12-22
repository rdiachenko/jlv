package com.github.rd.jlv.ui.preferences.additional;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;

public class LogLevelModel {

	private String levelName;

	private Image image;

	private RGB foreground;

	private RGB background;

	public LogLevelModel(String levelName, Image image, RGB foreground, RGB background) {
		this.levelName = levelName;
		this.image = image;
		this.foreground = foreground;
		this.background = background;
	}

	public RGB getForeground() {
		return foreground;
	}

	public void setForeground(RGB foreground) {
		this.foreground = foreground;
	}

	public RGB getBackground() {
		return background;
	}

	public void setBackground(RGB background) {
		this.background = background;
	}

	public String getLevelName() {
		return levelName;
	}

	public Image getImage() {
		return image;
	}

	@Override
	public String toString() {
		return "LogLevelItem [levelName=" + levelName + ", foreground=" + foreground + ", background=" + background
				+ "]";
	}
}
