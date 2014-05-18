package com.github.rd.jlv.model.converters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rd.jlv.log4j.LogConstants;
import com.github.rd.jlv.model.PresentationalModel;
import com.github.rd.jlv.model.PresentationalModel.ModelItem;
import com.github.rd.jlv.model.PresentationalModel.ModelItem.Rgb;

public class PresentationalModelConverter {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final ObjectMapper mapper = new ObjectMapper();

	public String defaultModelAsJson() {
		List<ModelItem> items = new ArrayList<>();
		items.add(createItem(LogConstants.DEBUG_LEVEL_NAME, createRgb(0, 0, 0), createRgb(255, 255, 255)));
		items.add(createItem(LogConstants.INFO_LEVEL_NAME, createRgb(0, 255, 0), createRgb(255, 255, 255)));
		items.add(createItem(LogConstants.WARN_LEVEL_NAME, createRgb(255, 128, 0), createRgb(255, 255, 255)));
		items.add(createItem(LogConstants.ERROR_LEVEL_NAME, createRgb(255, 0, 0), createRgb(255, 255, 255)));
		items.add(createItem(LogConstants.FATAL_LEVEL_NAME, createRgb(165, 42, 42), createRgb(255, 255, 255)));

		PresentationalModel model = new PresentationalModel();
		model.setLevelAsImage(true);
		model.setFontSize(11);
		model.setModelItems(items);
		return modelAsJson(model);
	}

	public PresentationalModel getModel(String json) {
		try {
			PresentationalModel model = mapper.readValue(json, PresentationalModel.class);
			Map<String, ModelItem> itemsMap = new HashMap<>();

			for (ModelItem item : model.getModelItems()) {
				itemsMap.put(item.getLevelName(), item);
			}
			model.setModelItemsMap(itemsMap);
			return model;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		PresentationalModel model = new PresentationalModel();
		model.setModelItems(new ArrayList<ModelItem>());
		return model;
	}

	public String modelAsJson(PresentationalModel model) {
		try {
			return mapper.writeValueAsString(model);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}

	private ModelItem createItem(String levelName, Rgb foreground, Rgb background) {
		ModelItem item = new ModelItem();
		item.setLevelName(levelName);
		item.setForeground(foreground);
		item.setBackground(background);
		return item;
	}

	private Rgb createRgb(int red, int green, int blue) {
		Rgb rgb = new Rgb();
		rgb.setRed(red);
		rgb.setGreen(green);
		rgb.setBlue(blue);
		return rgb;
	}
}
