package com.github.rd.jlv.props;

import java.util.ArrayList;
import java.util.List;

public class LoglistColumnConverter implements PropertyConverter<List<LoglistColumn>> {

	private static final String COLON_SEPARATOR = ":";
	private static final String SEMICOLON_SEPARATOR = ";";

	private static final int NAME_INDEX = 0;
	private static final int VISIBLE_INDEX = 1;
	private static final int WIDTH_INDEX = 2;

	@Override
	public List<LoglistColumn> convertFromString(String value) {
		List<LoglistColumn> values = new ArrayList<>();

		for (String partial : value.trim().split(SEMICOLON_SEPARATOR)) {
			String[] attributes = partial.trim().split(COLON_SEPARATOR);
			String name = attributes[NAME_INDEX].trim();
			boolean visible = Boolean.parseBoolean(attributes[VISIBLE_INDEX].trim());
			int width = Integer.parseInt(attributes[WIDTH_INDEX].trim());
			values.add(new LoglistColumn(name, visible, width));
		}
		return values;
	}

	@Override
	public String convertToString(List<LoglistColumn> value) {
		StringBuilder values = new StringBuilder();

		for (LoglistColumn partial : value) {
			values.append(partial.getName()).append(COLON_SEPARATOR);
			values.append(partial.isVisible()).append(COLON_SEPARATOR);
			values.append(partial.getWidth()).append(SEMICOLON_SEPARATOR);
		}
		return values.toString();
	}
}
