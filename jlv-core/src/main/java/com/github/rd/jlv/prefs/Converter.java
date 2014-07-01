package com.github.rd.jlv.prefs;

public interface Converter<T> {

	T getDefaultModel();

	T jsonToModel(String json);

	String modelToJson(T model);
}
