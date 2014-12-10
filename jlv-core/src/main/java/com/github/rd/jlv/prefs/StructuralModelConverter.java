package com.github.rd.jlv.prefs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rd.jlv.LogConstants;
import com.github.rd.jlv.prefs.StructuralModel.ModelItem;

public class StructuralModelConverter extends Converter {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public Model getDefaultModel() {
		List<ModelItem> items = new ArrayList<>();
		items.add(createItem(LogConstants.LEVEL_FIELD_NAME, 55, true));
		items.add(createItem(LogConstants.CATEGORY_FIELD_NAME, 100, true));
		items.add(createItem(LogConstants.MESSAGE_FIELD_NAME, 100, true));
		items.add(createItem(LogConstants.LINE_FIELD_NAME, 100, true));
		items.add(createItem(LogConstants.DATE_FIELD_NAME, 100, true));
		items.add(createItem(LogConstants.THROWABLE_FIELD_NAME, 100, true));

		StructuralModel defaultModel = new StructuralModel();
		defaultModel.setModelItems(items);
		return defaultModel;
	}

	@Override
	public Model jsonToModel(String json) {
		try {
			return mapper.readValue(json, StructuralModel.class);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		StructuralModel model = new StructuralModel();
		model.setModelItems(new ArrayList<ModelItem>());
		return model;
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

	private ModelItem createItem(String name, int width, boolean display) {
		ModelItem item = new ModelItem();
		item.setName(name);
		item.setWidth(width);
		item.setDisplay(display);
		return item;
	}
}
