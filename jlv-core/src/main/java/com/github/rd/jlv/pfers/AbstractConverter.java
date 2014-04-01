package com.github.rd.jlv.pfers;

public interface AbstractConverter<T> {

    String defaultModelToJson();

    T jsonToModel(String json);

    String modelToJson(T model);
}
