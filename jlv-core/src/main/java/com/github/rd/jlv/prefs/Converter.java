package com.github.rd.jlv.prefs;

import org.codehaus.jackson.map.ObjectMapper;

public abstract class Converter {

	protected final ObjectMapper mapper = new ObjectMapper();

	public abstract Model getDefaultModel();

	public abstract Model jsonToModel(String json);

	public abstract String modelToJson(Model model);

	public static Converter get(PreferenceEnum type) {
		switch (type) {
		case JLV_GENERAL_SETTINGS:
			return new GeneralModelConverter();
		case LOG_LIST_STRUCTURAL_TABLE_SETTINGS:
			return new StructuralModelConverter();
		case LOG_LIST_PRESENTATIONAL_TABLE_SETTINGS:
			return new PresentationalModelConverter();
		default:
			throw new IllegalArgumentException("No such a preference type: " + type);
		}
	}
}
