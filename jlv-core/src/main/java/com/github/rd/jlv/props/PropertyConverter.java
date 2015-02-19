package com.github.rd.jlv.props;

public interface PropertyConverter<T> {

	T convertFromString(String value);
	
	String convertToString(T value);
}
