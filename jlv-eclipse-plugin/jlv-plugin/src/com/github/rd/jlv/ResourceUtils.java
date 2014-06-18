package com.github.rd.jlv;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;

import com.github.rd.jlv.log4j.LogConstants;
import com.github.rd.jlv.pfers.PresentationalModel.ModelItem.Rgb;

public final class ResourceUtils {

	public static final Rgb FOREGROUND = new Rgb(0, 0, 0);

	public static final Rgb BACKGROUND = new Rgb(255, 255, 255);

	private static final ResourceManager resourceManager = JlvActivator.getDefault().getResourceManager();

	private static final Map<String, ImageType> imageNameMap = new HashMap<>();

	static {
		imageNameMap.put(LogConstants.DEBUG_LEVEL_NAME, ImageType.DEBUG_LEVEL_ICON);
		imageNameMap.put(LogConstants.INFO_LEVEL_NAME, ImageType.INFO_LEVEL_ICON);
		imageNameMap.put(LogConstants.WARN_LEVEL_NAME, ImageType.WARN_LEVEL_ICON);
		imageNameMap.put(LogConstants.ERROR_LEVEL_NAME, ImageType.ERROR_LEVEL_ICON);
		imageNameMap.put(LogConstants.FATAL_LEVEL_NAME, ImageType.FATAL_LEVEL_ICON);
	}

	private ResourceUtils() {
		throw new IllegalStateException("This is an util class. The object should not be created.");
	}

	public static Image getImage(String name) {
		if (imageNameMap.containsKey(name)) {
			return getImage(imageNameMap.get(name));
		} else {
			throw new IllegalArgumentException("No image with such a name: " + name);
		}
	}

	public static Image getImage(ImageType type) {
		return resourceManager.getImage(type);
	}

	public static Color getColor(Rgb rgb) {
		return resourceManager.getColor(rgb);
	}

	public static Font getFont(int size) {
		return resourceManager.getFont(size);
	}

	public static RGB toSystemRgb(Rgb rgb) {
		return new RGB(rgb.getRed(), rgb.getGreen(), rgb.getBlue());
	}

	public static Rgb fromSystemRgb(RGB rgb) {
		return new Rgb(rgb.red, rgb.green, rgb.blue);
	}
}
