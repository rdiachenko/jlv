package com.github.rd.jlv.props;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JlvProperties {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static final PropertyConverter<List<LoglistColumn>> loglistColumnConverter = new LoglistColumnConverter();
	private static final PropertyConverter<List<LoglistLevelColor>> loglistLevelColorConverter = new LoglistLevelColorConverter();
	private static final PropertyConverter<Integer> integerConverter = new IntegerConverter();
	private static final PropertyConverter<Boolean> booleanConverter = new BooleanConverter();
	private static final Map<PropertyKey, PropertyConverter<?>> converters = new EnumMap<>(PropertyKey.class);
	
	private Properties store = new Properties();
	private File propertyFile;
	
	static {
		converters.put(PropertyKey.SERVER_PORT_KEY, integerConverter);
		converters.put(PropertyKey.SERVER_AUTOSTART_KEY, booleanConverter);
		converters.put(PropertyKey.LOGLIST_BUFFER_SIZE_KEY, integerConverter);
		converters.put(PropertyKey.LOGLIST_REFRESH_TIME_KEY, integerConverter);
		converters.put(PropertyKey.LOGLIST_QUICK_SEARCH_KEY, booleanConverter);
		converters.put(PropertyKey.LOGLIST_FONT_SIZE_KEY, integerConverter);
		converters.put(PropertyKey.LOGLIST_LEVEL_IMAGE_KEY, booleanConverter);
		converters.put(PropertyKey.LOGLIST_LEVEL_COLOR_KEY, loglistLevelColorConverter);
		converters.put(PropertyKey.LOGLIST_COLUMN_KEY, loglistColumnConverter);
	}
	
	public JlvProperties(File propertyFile) {
		if (isFileValid(propertyFile)) {
			this.propertyFile = propertyFile;
			
			try (InputStream in = new FileInputStream(propertyFile)) {
				store.load(in);
			} catch (IOException e) {
				logger.warn("Couldn't load jlv properties file {}. The default properties will be used instead.", propertyFile.getAbsolutePath(), e);
			}
		}
	}
	
	public <T> void save(PropertyKey key, T value) {
		PropertyConverter<T> converter = getConverter(key);
		store.setProperty(key.keyName(), converter.convertToString(value));
	}
	
	public <T> T load(PropertyKey key) {
		PropertyConverter<T> converter = getConverter(key);
		String value = store.getProperty(key.keyName(), key.defaultValue());
		return converter.convertFromString(value);
	}
	
	public <T> T loadDefault(PropertyKey key) {
		PropertyConverter<T> converter = getConverter(key);
		return converter.convertFromString(key.defaultValue());
	}
	
	public void persist() {
		if (isFileValid(propertyFile)) {
			try (OutputStream out = new FileOutputStream(propertyFile)) {
				store.store(out, null);
			} catch (IOException e) {
				logger.error("Couldn't persist jlv properties file {}.", propertyFile.getAbsolutePath(), e);
			}
		}
	}
	
	private boolean isFileValid(File file) {
		boolean valid = file != null && file.exists() && file.isFile();

		if (!valid) {
			logger.warn("jlv properties file {} is not valid.");
		}
		return valid;
	}

	@SuppressWarnings("unchecked")
	private static <T> PropertyConverter<T> getConverter(PropertyKey key) {
		return (PropertyConverter<T>) converters.get(key);
	}
}
