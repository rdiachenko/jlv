package com.github.rd.jlv.props;

public class BooleanConverter implements PropertyConverter<Boolean> {

	@Override
	public Boolean convertFromString(String value) {
		return Boolean.parseBoolean(value);
	}

	@Override
	public String convertToString(Boolean value) {
		return String.valueOf(value);
	}
}
