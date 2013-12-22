package com.github.rd.jlv.ui.preferences.additional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;

public class PresentationalPreferenceModel {

	private boolean isLevelAsImage;

	private int fontSize;

	private List<LogLevelModel> logLevelModelList;

	private Map<String, LogLevelModel> logLevelModelMap;

	public PresentationalPreferenceModel(boolean isLevelAsImage, int fontSize, List<LogLevelModel> logLevelModelList) {
		this.isLevelAsImage = isLevelAsImage;
		this.fontSize = fontSize;
		this.logLevelModelList = logLevelModelList;
		logLevelModelMap = new HashMap<>();

		for (LogLevelModel model : logLevelModelList) {
			logLevelModelMap.put(model.getLevelName(), model);
		}
	}

	public boolean isLevelAsImage() {
		return isLevelAsImage;
	}

	public void setLevelAsImage(boolean isLevelAsImage) {
		this.isLevelAsImage = isLevelAsImage;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public List<LogLevelModel> getLogLevelModelList() {
		return logLevelModelList;
	}

	public void setLogLevelModelList(List<LogLevelModel> logLevelModelList) {
		for (LogLevelModel model : logLevelModelList) {
			String levelName = model.getLevelName();
			logLevelModelMap.get(levelName).setForeground(model.getForeground());
			logLevelModelMap.get(levelName).setBackground(model.getBackground());
		}
	}

	public RGB getForeground(String levelName) {
		return logLevelModelMap.get(levelName).getForeground();
	}

	public RGB getBackground(String levelName) {
		return logLevelModelMap.get(levelName).getBackground();
	}

	public Image getImage(String levelName) {
		return logLevelModelMap.get(levelName).getImage();
	}

	@Override
	public String toString() {
		return "[isLevelAsImage=" + isLevelAsImage + "][fontSize=" + fontSize + "]";
	}
}
