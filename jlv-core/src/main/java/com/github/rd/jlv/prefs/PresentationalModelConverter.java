package com.github.rd.jlv.prefs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rd.jlv.LogConstants;
import com.github.rd.jlv.prefs.PresentationalModel.ModelItem;
import com.github.rd.jlv.prefs.PresentationalModel.ModelItem.Rgb;

public class PresentationalModelConverter extends Converter {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public Model getDefaultModel() {
		List<ModelItem> items = new ArrayList<>();
		items.add(createItem(LogConstants.DEBUG_LEVEL_NAME, createRgb(0, 0, 0), createRgb(255, 255, 255)));
		items.add(createItem(LogConstants.INFO_LEVEL_NAME, createRgb(0, 255, 0), createRgb(255, 255, 255)));
		items.add(createItem(LogConstants.WARN_LEVEL_NAME, createRgb(255, 128, 0), createRgb(255, 255, 255)));
		items.add(createItem(LogConstants.ERROR_LEVEL_NAME, createRgb(255, 0, 0), createRgb(255, 255, 255)));
		items.add(createItem(LogConstants.FATAL_LEVEL_NAME, createRgb(165, 42, 42), createRgb(255, 255, 255)));

		PresentationalModel defaultModel = new PresentationalModel();
		defaultModel.setLevelAsImage(true);
		defaultModel.setFontSize(11);
		defaultModel.setModelItems(items);
		return defaultModel;
	}

	@Override
	public Model jsonToModel(String json) {
		try {
			PresentationalModel model = mapper.readValue(json, PresentationalModel.class);
			return model;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		PresentationalModel model = new PresentationalModel();
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
