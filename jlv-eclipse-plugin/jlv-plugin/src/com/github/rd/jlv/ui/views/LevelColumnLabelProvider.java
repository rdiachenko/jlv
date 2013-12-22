package com.github.rd.jlv.ui.views;

import org.eclipse.jface.viewers.OwnerDrawLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
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
import com.github.rd.jlv.log4j.domain.Log;

public class LevelColumnLabelProvider extends OwnerDrawLabelProvider {

	private Table table;

	public LevelColumnLabelProvider(Table table) {
		super();
		this.table = table;
	}

	@Override
	public void measure(Event event, Object element) {
		// no code
	}

	@Override
	public void update(ViewerCell cell) {
		Log log = (Log) cell.getElement();
		cell.setBackground(getBackground(log));
		cell.setForeground(getForeground(log));
		cell.setFont(getFont(log));
		super.update(cell);
	}

	@Override
	public void paint(Event event, Object element) {
		Log log = (Log) element;
		Rectangle bounds = ((TableItem) event.item).getBounds(event.index);

		if (JlvActivator.getPreferenceManager().isLevelAsImage()) {
			Image image = JlvActivator.getPreferenceManager().getLevelImage(log.getLevel());
			Rectangle imageBounds = image.getBounds();
			int xOffset = bounds.width / 2 - imageBounds.width / 2;
			int yOffset = bounds.height / 2 - imageBounds.height / 2;
			int x = xOffset > 0 ? bounds.x + xOffset : bounds.x;
			int y = yOffset > 0 ? bounds.y + yOffset : bounds.y;
			event.gc.drawImage(image, x, y);
		} else {
			Point point = event.gc.stringExtent(log.getLevel());
			int xOffset = bounds.width / 2 - point.x / 2;
			int yOffset = bounds.height / 2 - point.y / 2;
			int x = xOffset > 0 ? bounds.x + xOffset : bounds.x;
			int y = yOffset > 0 ? bounds.y + yOffset : bounds.y;
			event.gc.drawText(log.getLevel(), x, y, true);
		}
	}

	@Override
	public void erase(Event event, Object element) {
		if ((event.detail & SWT.SELECTED) != 0) {
			event.detail &= ~SWT.FOREGROUND;
		}
	}

	private Color getBackground(Log log) {
		RGB rgb = JlvActivator.getPreferenceManager().getBackground(log.getLevel());
		Color color = new Color(Display.getCurrent(), rgb);
		return color;
	}

	private Color getForeground(Log log) {
		RGB rgb = JlvActivator.getPreferenceManager().getForeground(log.getLevel());
		Color color = new Color(Display.getCurrent(), rgb);
		return color;
	}

	private Font getFont(Log log) {
		FontData[] fontData = table.getFont().getFontData();
		for (int i = 0; i < fontData.length; ++i) {
			fontData[i].setHeight(JlvActivator.getPreferenceManager().getFontSize());
		}
		Font font = new Font(Display.getCurrent(), fontData);
		return font;
	}
}
