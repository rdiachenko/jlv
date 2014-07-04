package com.github.rd.jlv.prefs;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeneralModelConverter extends Converter {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public Model getDefaultModel() {
		GeneralModel defaultModel = new GeneralModel();
		defaultModel.setPortNumber(4445);
		defaultModel.setAutoStart(true);
		defaultModel.setQuickSearch(true);
		defaultModel.setBufferSize(1000);
		defaultModel.setRefreshingTime(500);
		return defaultModel;
	}

	@Override
	public Model jsonToModel(String json) {
		try {
			GeneralModel model = mapper.readValue(json, GeneralModel.class);
			return model;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return new GeneralModel();
	}

	@Override
	public String modelToJson(Model model) {
		try {
			return mapper.writeValueAsString(model);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}
}
