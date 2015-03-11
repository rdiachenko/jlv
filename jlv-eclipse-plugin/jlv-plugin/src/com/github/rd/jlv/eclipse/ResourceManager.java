package com.github.rd.jlv.eclipse;

import java.net.URL;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rd.jlv.LogConstants;
import com.github.rd.jlv.props.LevelColor;

public final class ResourceManager {

//	public static final Rgb FOREGROUND = new Rgb(0, 0, 0);
//
//	public static final Rgb BACKGROUND = new Rgb(255, 255, 255);

	private final Map<String, JlvImage> images;
	private final InternalManager internalManager;

	public ResourceManager() {
		internalManager = new InternalManager();
		images = new HashMap<>();
		images.put(LogConstants.DEBUG_LEVEL_NAME, JlvImage.DEBUG_LEVEL_ICON);
		images.put(LogConstants.INFO_LEVEL_NAME, JlvImage.INFO_LEVEL_ICON);
		images.put(LogConstants.WARN_LEVEL_NAME, JlvImage.WARN_LEVEL_ICON);
		images.put(LogConstants.ERROR_LEVEL_NAME, JlvImage.ERROR_LEVEL_ICON);
		images.put(LogConstants.FATAL_LEVEL_NAME, JlvImage.FATAL_LEVEL_ICON);
	}

	public Image getImage(String name) {
		if (images.containsKey(name)) {
			return getImage(images.get(name));
		} else {
			throw new IllegalArgumentException("No image with such a name: " + name);
		}
	}

	public Image getImage(JlvImage jlvImage) {
		return internalManager.getImage(jlvImage);
	}

	public Color getColor(LevelColor rgb) {
		return internalManager.getColor(rgb);
	}

	public Font getFont(int size) {
		return internalManager.getFont(size);
	}

	public RGB toSystemRgb(LevelColor rgb) {
		return new RGB(rgb.getRed(), rgb.getGreen(), rgb.getBlue());
	}

	public LevelColor fromSystemRgb(RGB rgb) {
		return new LevelColor(rgb.red, rgb.green, rgb.blue);
	}

	public void dispose() {
		internalManager.dispose();
	}

	private static final class InternalManager {

		private final Logger logger = LoggerFactory.getLogger(getClass());

		private final Map<JlvImage, Image> imageRegistry = new EnumMap<>(JlvImage.class);
		private final Map<LevelColor, Color> colorRegistry = new HashMap<>();
		private final Map<Integer, Font> fontRegistry = new HashMap<>();

		private Font systemFont;
		private FontData[] systemFontData;

		public InternalManager() {
			loadImagesToRegistry();
		}

		public Image getImage(JlvImage type) {
			return imageRegistry.get(type);
		}

		public Color getColor(LevelColor rgb) {
			Color color = colorRegistry.get(rgb);

			if (color == null || color.isDisposed()) {
				color = new Color(Display.getCurrent(), rgb.getRed(), rgb.getGreen(), rgb.getBlue());
				colorRegistry.put(rgb, color);
				logger.debug("A new color was added to color registry: {}", color);
			}
			return color;
		}

		public Font getFont(int fontSize) {
			Font font = fontRegistry.get(fontSize);

			if (font == null || font.isDisposed()) {
				Display display = Display.getCurrent();
				font = new Font(display, getFontData(display, fontSize));
				fontRegistry.put(fontSize, font);
				logger.debug("A new font was added to font registry: {}", font);
			}
			return font;
		}

		public void dispose() {
			dispose(imageRegistry);
			logger.debug("Image registry was disposed: {}", imageRegistry.isEmpty());
			dispose(colorRegistry);
			logger.debug("Color registry was disposed: {}", colorRegistry.isEmpty());
			dispose(fontRegistry);
			logger.debug("Font registry was disposed: {}", fontRegistry.isEmpty());
		}

		private <V extends Resource> void dispose(Map<?, V> resource) {
			for (V value : resource.values()) {
				value.dispose();
			}
			resource.clear();
		}

		private void loadImagesToRegistry() {
			Bundle bundle = JlvActivator.getDefault().getBundle();

			for (JlvImage imageType : JlvImage.values()) {
				URL url = bundle.getEntry(imageType.path());
				ImageDescriptor descriptor = ImageDescriptor.createFromURL(url);
				imageRegistry.put(imageType, descriptor.createImage());
			}
		}

		private FontData[] getFontData(Display display, int fontSize) {
			if (systemFont == null || systemFont.isDisposed()) {
				systemFont = display.getSystemFont();
				systemFontData = systemFont.getFontData();
			}

			for (FontData data : systemFontData) {
				data.setHeight(fontSize);
			}
			return systemFontData;
		}
	}
}
