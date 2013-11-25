package com.github.rd.jlv.ui.preferences.additional;

import java.util.Map;

import org.eclipse.swt.graphics.RGB;

import com.github.rd.jlv.model.LogLevel;

public class LogsDisplayModel {

	private boolean levelImageSubstitutesText;

	private int fontSize;

	private Map<LogLevel, LogLevelItem> logLevelPrefsMap;

	public LogsDisplayModel(boolean levelImageSubstitutesText, int fontSize,
			Map<LogLevel, LogLevelItem> logLevelPrefsMap) {
		this.levelImageSubstitutesText = levelImageSubstitutesText;
		this.fontSize = fontSize;
		this.logLevelPrefsMap = logLevelPrefsMap;
	}

	public boolean isLevelImageSubstitutesText() {
		return levelImageSubstitutesText;
	}

	public void setLevelImageSubstitutesText(boolean levelImageSubstitutesText) {
		this.levelImageSubstitutesText = levelImageSubstitutesText;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public LogLevelItem[] getLogLevelItems() {
		LogLevelItem[] logLevelItems = new LogLevelItem[LogLevel.values().length];
		int index = 0;

		for (LogLevel level : LogLevel.values()) {
			logLevelItems[index] = logLevelPrefsMap.get(level);
			index++;
		}
		return logLevelItems;
	}

	public void setLogLevelItems(LogLevelItem[] items) {
		for (int i = 0; i < items.length; i++) {
			LogLevel level = LogLevel.getLogLevelByName(items[i].getName());
			logLevelPrefsMap.get(level).setForeground(items[i].getForeground());
			logLevelPrefsMap.get(level).setBackground(items[i].getBackground());
		}
	}

	public RGB getForegroundByLevel(LogLevel logLevel) {
		return logLevelPrefsMap.get(logLevel).getForeground();
	}

	public RGB getBackgroundByLevel(LogLevel logLevel) {
		return logLevelPrefsMap.get(logLevel).getBackground();
	}

	@Override
	public String toString() {
		return "[levelImageSubstitutesText=" + levelImageSubstitutesText + "][fontSize=" + fontSize + "]";
	}
}
