package com.github.rd.jlv.pfers;

public interface AbstractConverter<T> {

	T getDefaultModel();

	T jsonToModel(String json);

	String modelToJson(T model);
}
