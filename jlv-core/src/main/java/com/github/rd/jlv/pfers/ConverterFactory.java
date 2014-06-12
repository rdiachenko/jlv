package com.github.rd.jlv.pfers;

import java.util.HashMap;
import java.util.Map;

public final class ConverterFactory {

	private static final Map<Class<?>, Converter<?>> CONVERTERS = new HashMap<>();

	private ConverterFactory() {
		throw new IllegalStateException("This is a factory class. The object should not be created.");
	}

	@SuppressWarnings("unchecked")
	public static <T> Converter<T> getConverter(Class<T> modelType) {
		Converter<?> converter;

		if (CONVERTERS.containsKey(modelType)) {
			converter = CONVERTERS.get(modelType);
		} else {
			if (modelType.isAssignableFrom(GeneralModel.class)) {
				converter = new GeneralModelConverter();
			} else if (modelType.isAssignableFrom(StructuralModel.class)) {
				converter = new StructuralModelConverter();
			} else if (modelType.isAssignableFrom(PresentationalModel.class)) {
				converter = new PresentationalModelConverter();
			} else {
				throw new IllegalArgumentException("No such a model type: " + modelType);
			}
			CONVERTERS.put(modelType, converter);
		}
		return (Converter<T>) converter;
	}
}
