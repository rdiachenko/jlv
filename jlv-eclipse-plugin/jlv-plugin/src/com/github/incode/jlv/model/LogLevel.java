package com.github.incode.jlv.model;

import org.eclipse.swt.graphics.RGB;

import com.github.incode.jlv.ImageType;

public enum LogLevel {

	DEBUG("DEBUG", ImageType.DEBUG, new RGB(0, 0, 0), new RGB(237, 236, 235)),
	INFO("INFO", ImageType.INFO, new RGB(0, 255, 0), new RGB(237, 236, 235)),
	WARN("WARN", ImageType.WARN, new RGB(255, 128, 0), new RGB(237, 236, 235)),
	ERROR("ERROR", ImageType.ERROR, new RGB(255, 0, 0), new RGB(237, 236, 235)),
	FATAL("FATAL", ImageType.FATAL, new RGB(165, 42, 42), new RGB(237, 236, 235));

	private String name;

	private ImageType image;

	private RGB foreground;

	private RGB background;

	private LogLevel(String name, ImageType image, RGB foreground, RGB background) {
		this.name = name;
		this.image = image;
		this.foreground = foreground;
		this.background = background;
	}

	public String getName() {
		return name;
	}

	public ImageType image() {
		return image;
	}

	public RGB foreground() {
		return foreground;
	}

	public RGB background() {
		return background;
	}

	public static ImageType getImageByName(String name) {
		switch (name) {
		case "DEBUG":
			return LogLevel.DEBUG.image();
		case "INFO":
			return LogLevel.INFO.image();
		case "WARN":
			return LogLevel.WARN.image();
		case "ERROR":
			return LogLevel.ERROR.image();
		case "FATAL":
			return LogLevel.FATAL.image();
		default:
			throw new IllegalArgumentException("No log level with such name: " + name
					+ ". Available range is [DEBUG, INFO, WARN, ERROR, FATAL]");
		}
	}
}
