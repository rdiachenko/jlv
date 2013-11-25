package com.github.rd.jlv.ui.preferences.additional;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;

public class LogLevelItem {

	private String name;

	private Image image;

	private RGB foreground;

	private RGB background;

	public LogLevelItem(String name, Image image, RGB foreground, RGB background) {
		this.name = name;
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

	public String getName() {
		return name;
	}

	public Image getImage() {
		return image;
	}

	@Override
	public String toString() {
		return "[" + name + "][foreground-" + foreground.toString() + "][background-" + background.toString() + "]";
	}
}
