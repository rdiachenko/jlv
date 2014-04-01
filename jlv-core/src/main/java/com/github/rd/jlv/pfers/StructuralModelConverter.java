package com.github.rd.jlv.pfers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rd.jlv.log4j.LogConstants;
import com.github.rd.jlv.pfers.StructuralModel.ModelItem;

public class StructuralModelConverter
    implements AbstractConverter<StructuralModel> {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final ObjectMapper mapper = new ObjectMapper();

	@Override
    public String defaultModelToJson() {
		List<ModelItem> items = new ArrayList<>();
		items.add(createItem(LogConstants.LEVEL_FIELD_NAME, 55, true));
		items.add(createItem(LogConstants.CATEGORY_FIELD_NAME, 100, true));
		items.add(createItem(LogConstants.MESSAGE_FIELD_NAME, 100, true));
		items.add(createItem(LogConstants.LINE_FIELD_NAME, 100, true));
		items.add(createItem(LogConstants.DATE_FIELD_NAME, 100, true));
		items.add(createItem(LogConstants.THROWABLE_FIELD_NAME, 100, true));

		StructuralModel model = new StructuralModel();
		model.setModelItems(items);
		return modelToJson(model);
	}

	@Override
    public StructuralModel jsonToModel(String json) {
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
    public String modelToJson(StructuralModel model) {
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
