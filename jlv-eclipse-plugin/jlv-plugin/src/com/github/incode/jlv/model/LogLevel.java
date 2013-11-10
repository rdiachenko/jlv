package com.github.incode.jlv.model;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;

import com.github.incode.jlv.ImageType;
import com.github.incode.jlv.JlvActivator;

public enum LogLevel {

	DEBUG("DEBUG", ImageType.DEBUG, new RGB(0, 0, 0), new RGB(255, 255, 255)),
	INFO("INFO", ImageType.INFO, new RGB(0, 255, 0), new RGB(255, 255, 255)),
	WARN("WARN", ImageType.WARN, new RGB(255, 128, 0), new RGB(255, 255, 255)),
	ERROR("ERROR", ImageType.ERROR, new RGB(255, 0, 0), new RGB(255, 255, 255)),
	FATAL("FATAL", ImageType.FATAL, new RGB(165, 42, 42), new RGB(255, 255, 255));

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

	public static Image getImageByName(String name) {
		switch (name) {
		case "DEBUG":
			return JlvActivator.getImage(LogLevel.DEBUG.image());
		case "INFO":
			return JlvActivator.getImage(LogLevel.INFO.image());
		case "WARN":
			return JlvActivator.getImage(LogLevel.WARN.image());
		case "ERROR":
			return JlvActivator.getImage(LogLevel.ERROR.image());
		case "FATAL":
			return JlvActivator.getImage(LogLevel.FATAL.image());
		default:
			throw new IllegalArgumentException("No log level with such name: " + name
					+ ". Available range is [DEBUG, INFO, WARN, ERROR, FATAL]");
		}
	}

	public static LogLevel getLogLevelByName(String name) {
		switch (name) {
		case "DEBUG":
			return LogLevel.DEBUG;
		case "INFO":
			return LogLevel.INFO;
		case "WARN":
			return LogLevel.WARN;
		case "ERROR":
			return LogLevel.ERROR;
		case "FATAL":
			return LogLevel.FATAL;
		default:
			throw new IllegalArgumentException("No log level with such name: " + name
					+ ". Available range is [DEBUG, INFO, WARN, ERROR, FATAL]");
		}
	}
}
