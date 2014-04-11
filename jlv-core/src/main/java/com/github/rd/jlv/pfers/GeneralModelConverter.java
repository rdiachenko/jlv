package com.github.rd.jlv.pfers;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeneralModelConverter
implements AbstractConverter<GeneralModel> {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	public GeneralModel getDefaultModel() {
		GeneralModel model = new GeneralModel();
		model.setPortNumber(4445);
		model.setAutoStart(true);
		model.setQuickSearch(true);
		model.setBufferSize(1000);
		model.setRefreshingTime(500);
		return model;
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
