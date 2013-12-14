package com.github.rd.jlv.ui.views;

import org.eclipse.jface.viewers.OwnerDrawLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.github.rd.jlv.JlvActivator;
import com.github.rd.jlv.log4j.LogUtil;
import com.github.rd.jlv.log4j.domain.Log;
import com.github.rd.jlv.ui.LogLevel;

public class LevelColumnLabelProvider extends OwnerDrawLabelProvider {

	private Table table;

	private String name;

	public LevelColumnLabelProvider(Table table, String name) {
		super();
		this.table = table;
		this.name = name;
	}

	@Override
	protected void measure(Event event, Object element) {
		// no code
	}

	@Override
	protected void paint(Event event, Object element) {
		Log log = (Log) element;
		setBackground(event, log);
		setForeground(event, log);
		setFont(event, log);
		String value = LogUtil.getValue(log, name);
		Rectangle bounds = ((TableItem) event.item).getBounds(event.index);

		if (JlvActivator.getPreferenceManager().isLevelImageSubstitutesText()) {
			Image image = LogLevel.getImageByName(value);
			Rectangle imageBounds = image.getBounds();
			int xOffset = bounds.width / 2 - imageBounds.width / 2;
			int yOffset = bounds.height / 2 - imageBounds.height / 2;
			int x = xOffset > 0 ? bounds.x + xOffset : bounds.x;
			int y = yOffset > 0 ? bounds.y + yOffset : bounds.y;
			event.gc.drawImage(image, x, y);
		} else {
			Point point = event.gc.stringExtent(value);
			int xOffset = bounds.width / 2 - point.x / 2;
			int yOffset = bounds.height / 2 - point.y / 2;
			int x = xOffset > 0 ? bounds.x + xOffset : bounds.x;
			int y = yOffset > 0 ? bounds.y + yOffset : bounds.y;
			event.gc.drawText(value, x, y, true);
		}
	}

	private void setBackground(Event event, Log log) {
		RGB rgb = JlvActivator.getPreferenceManager().getLogsBackground(
				LogLevel.getLogLevelByName(log.getLevel()));
		Color color = new Color(Display.getCurrent(), rgb);
		event.gc.setBackground(color);
	}

	private void setForeground(Event event, Log log) {
		RGB rgb = JlvActivator.getPreferenceManager().getLogsForeground(
				LogLevel.getLogLevelByName(log.getLevel()));
		Color color = new Color(Display.getCurrent(), rgb);
		event.gc.setForeground(color);
	}

	private void setFont(Event event, Log log) {
		FontData[] fontData = table.getFont().getFontData();
		for (int i = 0; i < fontData.length; ++i) {
			fontData[i].setHeight(JlvActivator.getPreferenceManager().getLogsFontSize());
		}
		Font font = new Font(Display.getCurrent(), fontData);
		event.gc.setFont(font);
	}
}
