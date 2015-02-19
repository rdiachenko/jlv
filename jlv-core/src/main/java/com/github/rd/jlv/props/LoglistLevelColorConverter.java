package com.github.rd.jlv.props;

import java.util.ArrayList;
import java.util.List;

public class LoglistLevelColorConverter implements PropertyConverter<List<LoglistLevelColor>> {

	private static final String COLON_SEPARATOR = ":";
	private static final String SEMICOLON_SEPARATOR = ";";
	private static final String DASH_SEPARATOR = "-";
	
	private static final int NAME_INDEX = 0;
	private static final int FOREGROUND_INDEX = 1;
	private static final int BACKGROUND_INDEX = 2;
	
	private static final int RED_INDEX = 0;
	private static final int GREEN_INDEX = 1;
	private static final int BLUE_INDEX = 2;
	
	@Override
	public List<LoglistLevelColor> convertFromString(String value) {
		List<LoglistLevelColor> values = new ArrayList<>();
		
		for (String partial : value.trim().split(SEMICOLON_SEPARATOR)) {
			String[] attributes = partial.trim().split(COLON_SEPARATOR);
			String name = attributes[NAME_INDEX];
			LevelColor foreground = stringToColor(attributes[FOREGROUND_INDEX]);
			LevelColor background = stringToColor(attributes[BACKGROUND_INDEX]);
			values.add(new LoglistLevelColor(name, foreground, background));
		}
		return values;
	}

	@Override
	public String convertToString(List<LoglistLevelColor> value) {
		StringBuilder values = new StringBuilder();
		
		for (LoglistLevelColor partial : value) {
			values.append(partial.getName()).append(COLON_SEPARATOR);
			values.append(colorToString(partial.getForeground())).append(COLON_SEPARATOR);
			values.append(colorToString(partial.getBackground())).append(SEMICOLON_SEPARATOR);
		}
		return values.toString();
	}
	
	private static LevelColor stringToColor(String value) {
		String[] partial = value.split(DASH_SEPARATOR);
		int red = Integer.parseInt(partial[RED_INDEX].trim());
		int green = Integer.parseInt(partial[GREEN_INDEX].trim());
		int blue = Integer.parseInt(partial[BLUE_INDEX].trim());
		return new LevelColor(red, green, blue);
	}
	
	private static String colorToString(LevelColor color) {
		StringBuilder value = new StringBuilder();
		value.append(color.getRed()).append(DASH_SEPARATOR);
		value.append(color.getGreen()).append(DASH_SEPARATOR);
		value.append(color.getBlue());
		return value.toString();
	}
}
