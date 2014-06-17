package com.github.rd.jlv.pfers;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeneralModelConverter implements Converter<GeneralModel> {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final ObjectMapper mapper = new ObjectMapper();

	private GeneralModel defaultModel;

	@Override
	public GeneralModel getDefaultModel() {
		if (defaultModel == null) {
			defaultModel = new GeneralModel();
			defaultModel.setPortNumber(4445);
			defaultModel.setAutoStart(true);
			defaultModel.setQuickSearch(true);
			defaultModel.setBufferSize(1000);
			defaultModel.setRefreshingTime(500);
		}
		return defaultModel;
	}

	@Override
	public GeneralModel jsonToModel(String json) {
		try {
			GeneralModel model = mapper.readValue(json, GeneralModel.class);
			return model;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return new GeneralModel();
	}

	@Override
	public String modelToJson(GeneralModel model) {
		try {
			return mapper.writeValueAsString(model);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}
}
