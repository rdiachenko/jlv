package com.github.rd.jlv.ui.preferences.additional;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;

import com.github.rd.jlv.ImageType;
import com.github.rd.jlv.JlvActivator;
import com.github.rd.jlv.log4j.LogConstants;
import com.google.common.base.Strings;

public class PresentationalPreferenceManager {

	private static final String COLUMN_SEPARATOR = ":";
	private static final String SEMICOLUMN_SEPARATOR = ";";
	private static final String DASH_SEPARATOR = "-";

	private IPreferenceStore store;
	private String preferenceName;

	public PresentationalPreferenceManager(IPreferenceStore store, String preferenceName) {
		this.store = store;
		this.preferenceName = preferenceName;
	}

	public PresentationalPreferenceModel loadModel() {
		String prefs = store.getString(preferenceName);

		if (Strings.isNullOrEmpty(prefs)) {
			return loadDefaultModel();
		} else {
			return stringToModel(prefs);
		}
	}

	public PresentationalPreferenceModel loadModel(String model) {
		return stringToModel(model);
	}

	public PresentationalPreferenceModel loadDefaultModel() {
		String prefs = store.getDefaultString(preferenceName);
		return stringToModel(prefs);
	}

	public void storeModel(PresentationalPreferenceModel model) {
		String prefs = modelToString(model);
		store.setValue(preferenceName, prefs);
	}

	private PresentationalPreferenceModel stringToModel(String prefs) {
		String[] modelItems = prefs.split(SEMICOLUMN_SEPARATOR);
		List<LogLevelModel> logLevelModelList = new ArrayList<>();

		for (int i = 2; i < modelItems.length; i++) {
			String[] logLevelItems = modelItems[i].split(COLUMN_SEPARATOR);
			String levelName = logLevelItems[0];
			RGB foreground = stringToRgb(logLevelItems[1]);
			RGB background = stringToRgb(logLevelItems[2]);
			LogLevelModel logLevelModel = new LogLevelModel(levelName, levelNameToImage(levelName), foreground,
					background);
			logLevelModelList.add(logLevelModel);
		}
		boolean isLevelAsImage = Boolean.parseBoolean(modelItems[0]);
		int fontSize = Integer.parseInt(modelItems[1]);
		return new PresentationalPreferenceModel(isLevelAsImage, fontSize, logLevelModelList);
	}

	private RGB stringToRgb(String value) {
		String[] colors = value.split(DASH_SEPARATOR);
		int red = Integer.parseInt(colors[0]);
		int green = Integer.parseInt(colors[1]);
		int blue = Integer.parseInt(colors[2]);
		RGB rgb = new RGB(red, green, blue);
		return rgb;
	}

	private String rgbToString(RGB rgb) {
		StringBuilder builder = new StringBuilder();
		builder.append(rgb.red).append(DASH_SEPARATOR)
				.append(rgb.green).append(DASH_SEPARATOR)
				.append(rgb.blue);
		return builder.toString();
	}

	private String modelToString(PresentationalPreferenceModel model) {
		StringBuilder builder = new StringBuilder();
		builder.append(model.isLevelAsImage()).append(SEMICOLUMN_SEPARATOR)
				.append(model.getFontSize()).append(SEMICOLUMN_SEPARATOR);

		for (LogLevelModel levelItem : model.getLogLevelModelList()) {
			builder.append(levelItem.getLevelName()).append(COLUMN_SEPARATOR)
					.append(rgbToString(levelItem.getForeground())).append(COLUMN_SEPARATOR)
					.append(rgbToString(levelItem.getBackground())).append(SEMICOLUMN_SEPARATOR);
		}
		return builder.toString();
	}

	private Image levelNameToImage(String levelName) {
		switch (levelName) {
		case LogConstants.DEBUG_LEVEL_NAME:
			return JlvActivator.getImage(ImageType.DEBUG);
		case LogConstants.INFO_LEVEL_NAME:
			return JlvActivator.getImage(ImageType.INFO);
		case LogConstants.WARN_LEVEL_NAME:
			return JlvActivator.getImage(ImageType.WARN);
		case LogConstants.ERROR_LEVEL_NAME:
			return JlvActivator.getImage(ImageType.ERROR);
		case LogConstants.FATAL_LEVEL_NAME:
			return JlvActivator.getImage(ImageType.FATAL);
		default:
			throw new IllegalArgumentException("No log level with such name: " + levelName);
		}
	}
}
