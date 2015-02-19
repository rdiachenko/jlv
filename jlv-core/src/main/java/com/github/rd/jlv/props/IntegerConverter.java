package com.github.rd.jlv.props;

public class IntegerConverter implements PropertyConverter<Integer> {

	@Override
	public Integer convertFromString(String value) {
		return Integer.parseInt(value.trim());
	}

	@Override
	public String convertToString(Integer value) {
		return String.valueOf(value);
	}
}
