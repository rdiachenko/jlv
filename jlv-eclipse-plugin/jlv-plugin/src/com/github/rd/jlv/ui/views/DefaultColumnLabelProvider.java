package com.github.rd.jlv.ui.views;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;

import com.github.rd.jlv.JlvActivator;
import com.github.rd.jlv.log4j.LogUtil;
import com.github.rd.jlv.log4j.domain.Log;
import com.github.rd.jlv.ui.LogLevel;

public class DefaultColumnLabelProvider extends ColumnLabelProvider {

	private Table table;

	private String fieldName;

	public DefaultColumnLabelProvider(Table table, String fieldName) {
		super();
		this.table = table;
		this.fieldName = fieldName;
	}

	@Override
	public String getText(Object element) {
		Log log = (Log) element;
		return LogUtil.getValue(log, fieldName);
	}

	@Override
	public Image getImage(Object element) {
		return null;
	}

	@Override
	public Color getForeground(Object element) {
		Log log = (Log) element;
		RGB rgb = JlvActivator.getPreferenceManager().getLogsForeground(
				LogLevel.getLogLevelByName(log.getLevel()));
		Color color = new Color(Display.getCurrent(), rgb);
		return color;
	}

	@Override
	public Color getBackground(Object element) {
		Log log = (Log) element;
		RGB rgb = JlvActivator.getPreferenceManager().getLogsBackground(
				LogLevel.getLogLevelByName(log.getLevel()));
		Color color = new Color(Display.getCurrent(), rgb);
		return color;
	}

	@Override
	public Font getFont(Object element) {
		FontData[] fontData = table.getFont().getFontData();
		for (int i = 0; i < fontData.length; ++i) {
			fontData[i].setHeight(JlvActivator.getPreferenceManager().getLogsFontSize());
		}
		Font font = new Font(Display.getCurrent(), fontData);
		return font;
	}
}
