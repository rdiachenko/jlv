package com.github.incode.jlv.ui.preferences.additional;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.RGB;

import com.github.incode.jlv.JlvActivator;
import com.github.incode.jlv.model.LogLevel;
import com.google.common.base.Strings;

public class LogsDisplayPreferenceManager {

	private static final String COLUMN_SEPARATOR = ":";
	private static final String SEMICOLUMN_SEPARATOR = ";";
	private static final String DASH_SEPARATOR = "-";

	private IPreferenceStore store;
	private String preferenceName;

	public LogsDisplayPreferenceManager(IPreferenceStore store, String preferenceName) {
		this.store = store;
		this.preferenceName = preferenceName;
	}

	public LogsDisplayModel loadModel() {
		String prefs = store.getString(preferenceName);

		if (Strings.isNullOrEmpty(prefs)) {
			return loadDefaultModel();
		} else {
			return stringToModel(prefs);
		}
	}

	public LogsDisplayModel loadModel(String model) {
		return stringToModel(model);
	}

	public LogsDisplayModel loadDefaultModel() {
		String prefs = store.getDefaultString(preferenceName);
		return stringToModel(prefs);
	}

	public void storeModel(LogsDisplayModel model) {
		String prefs = modelToString(model);
		store.setValue(preferenceName, prefs);
	}

	private LogsDisplayModel stringToModel(String prefs) {
		String[] modelParams = prefs.split(SEMICOLUMN_SEPARATOR);
		boolean levelImageSubstitutesTextParam = Boolean.parseBoolean(modelParams[0]);
		int fontSizeParam = Integer.parseInt(modelParams[1]);
		int logLevelItemsParamStartIndex = 2;
		LogLevelItem[] logLevelItems = new LogLevelItem[modelParams.length - logLevelItemsParamStartIndex];

		for (int i = 0; i < modelParams.length - logLevelItemsParamStartIndex; i++) {
			String[] logLevelItemsParams = modelParams[i + logLevelItemsParamStartIndex].split(COLUMN_SEPARATOR);
			String name = logLevelItemsParams[0];
			RGB foreground = stringToRgb(logLevelItemsParams[1]);
			RGB background = stringToRgb(logLevelItemsParams[2]);
			logLevelItems[i] = new LogLevelItem(name, JlvActivator.getImage(LogLevel.getImageByName(name)), foreground,
					background);
		}
		LogsDisplayModel model = new LogsDisplayModel(levelImageSubstitutesTextParam, fontSizeParam,
				logLevelItems);
		return model;
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

	private String modelToString(LogsDisplayModel model) {
		StringBuilder builder = new StringBuilder();
		builder.append(model.isLevelImageSubstitutesText()).append(SEMICOLUMN_SEPARATOR)
				.append(model.getFontSize()).append(SEMICOLUMN_SEPARATOR);

		for (LogLevelItem levelItem : model.getLogLevelItems()) {
			builder.append(levelItem.getName()).append(COLUMN_SEPARATOR)
					.append(rgbToString(levelItem.getForeground())).append(COLUMN_SEPARATOR)
					.append(rgbToString(levelItem.getBackground())).append(SEMICOLUMN_SEPARATOR);
		}
		return builder.toString();
	}
}
