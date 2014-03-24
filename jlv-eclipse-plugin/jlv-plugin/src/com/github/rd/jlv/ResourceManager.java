package com.github.rd.jlv;

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

import com.github.rd.jlv.model.PresentationalModel.ModelItem.Rgb;

public class ResourceManager {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final Map<ImageType, Image> imageRegistry = new EnumMap<>(ImageType.class);

	private final Map<Rgb, Color> colorRegistry = new HashMap<>();

	private final Map<Integer, Font> fontRegistry = new HashMap<>();

	private Font systemFont;

	private FontData[] systemFontData;

	public ResourceManager() {
		loadImagesToRegistry();
	}

	public Image getImage(ImageType type) {
		return imageRegistry.get(type);
	}

	public Color getColor(Display display, Rgb rgb) {
		Color color = colorRegistry.get(rgb);

		if (color == null || color.isDisposed()) {
			color = new Color(display, new RGB(rgb.getRed(), rgb.getGreen(), rgb.getBlue()));
			colorRegistry.put(rgb, color);
			logger.debug("A new color was added to color registry: {}", color);
		}
		return color;
	}

	public Font getFont(Display display, int fontSize) {
		Font font = fontRegistry.get(fontSize);

		if (font == null || font.isDisposed()) {
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

		for (ImageType imageType : ImageType.values()) {
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
