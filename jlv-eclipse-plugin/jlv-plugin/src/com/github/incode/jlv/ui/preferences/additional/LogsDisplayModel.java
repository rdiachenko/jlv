package com.github.incode.jlv.ui.preferences.additional;

public class LogsDisplayModel {

	private boolean levelImageSubstitutesText;

	private int fontSize;

	private LogLevelItem[] logLevelItems;

	public LogsDisplayModel(boolean levelImageSubstitutesText, int fontSize, LogLevelItem[] logLevelItems) {
		this.levelImageSubstitutesText = levelImageSubstitutesText;
		this.fontSize = fontSize;
		this.logLevelItems = logLevelItems;
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
		return logLevelItems;
	}

	public void setLogLevelItems(LogLevelItem[] items) {
		for (int i = 0; i < logLevelItems.length; i++) {
			logLevelItems[i].setForeground(items[i].getForeground());
			logLevelItems[i].setBackground(items[i].getBackground());
		}
	}

	@Override
	public String toString() {
		return "[levelImageSubstitutesText=" + levelImageSubstitutesText + "][fontSize=" + fontSize + "]";
	}
}
