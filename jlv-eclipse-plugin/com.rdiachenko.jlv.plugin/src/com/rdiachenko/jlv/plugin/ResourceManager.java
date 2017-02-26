package com.rdiachenko.jlv.plugin;

import java.net.URL;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rdiachenko.jlv.plugin.preference.Rgb;

public final class ResourceManager {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceManager.class);

    private static final Map<FontPair, Font> FONTS = new HashMap<>();
    private static final Map<Rgb, Color> COLORS = new HashMap<>();
    private static final Map<ImageResource, Image> IMAGES = new EnumMap<>(ImageResource.class);
    
    private ResourceManager() {
        // Utility class
    }

    public static Font getFont(Composite source, int fontSize, int style) {
        FontPair pair = new FontPair(fontSize, style);
        Font font = FONTS.get(pair);

        if (font == null || font.isDisposed()) {
            Font baseFont = source.getFont();
            FontData[] fontData = baseFont.getFontData();

            for (FontData data : fontData) {
                data.setHeight(fontSize);
                data.setStyle(style);
            }
            font = new Font(source.getDisplay(), fontData);
            FONTS.put(pair, font);
            LOGGER.info("A new font was added to font registry: {}", pair);
        }
        return font;
    }

    public static Color getColor(Display display, Rgb rgb) {
        Color color = COLORS.get(rgb);
        
        if (color == null || color.isDisposed()) {
            color = new Color(display, rgb.getRed(), rgb.getGreen(), rgb.getBlue());
            COLORS.put(rgb, color);
            LOGGER.info("A new color was added to color registry: {}", rgb);
        }
        return color;
    }

    public static Image getImage(ImageResource key) {
        Image image = IMAGES.get(key);

        if (image == null || image.isDisposed()) {
            Bundle bundle = JlvActivator.getDefault().getBundle();
            URL url = bundle.getEntry(key.path());
            ImageDescriptor descriptor = ImageDescriptor.createFromURL(url);
            image = descriptor.createImage();
            IMAGES.put(key, image);
            LOGGER.info("A new image was added to image registry: {}", key.path());
        }
        return image;
    }
    
    public static Image getImage(LogLevel level) {
        Image image;
        
        switch (level) {
        case DEBUG:
            image = getImage(ImageResource.DEBUG_LOG_LEVEL_ICON);
            break;
        case INFO:
            image = getImage(ImageResource.INFO_LOG_LEVEL_ICON);
            break;
        case WARN:
            image = getImage(ImageResource.WARN_LOG_LEVEL_ICON);
            break;
        case ERROR:
            image = getImage(ImageResource.ERROR_LOG_LEVEL_ICON);
            break;
        case FATAL:
            image = getImage(ImageResource.FATAL_LOG_LEVEL_ICON);
            break;
        default:
            image = getImage(ImageResource.OTHER_LOG_LEVEL_ICON);
        }
        return image;
    }

    public static void dispose() {
        dispose(FONTS);
        LOGGER.info("Font registry disposed");
        dispose(COLORS);
        LOGGER.info("Color registry disposed");
        dispose(IMAGES);
        LOGGER.info("Image registry disposed");
    }
    
    private static <V extends Resource> void dispose(Map<?, V> resource) {
        for (V value : resource.values()) {
            value.dispose();
        }
        resource.clear();
    }
    
    private static final class FontPair {
        
        private final int size;
        private final int style;
        
        public FontPair(int size, int style) {
            this.size = size;
            this.style = style;
        }

        @Override
        public int hashCode() {
            return Objects.hash(size, style);
        }
        
        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }
            if (other == null || getClass() != other.getClass()) {
                return false;
            }
            FontPair pair = (FontPair) other;
            return Objects.equals(size, pair.size)
                    && Objects.equals(style, pair.style);
        }
        
        @Override
        public String toString() {
            return FontPair.class.getName() + "[size=" + size + ", style=" + style + "]";
        }
    }
}
