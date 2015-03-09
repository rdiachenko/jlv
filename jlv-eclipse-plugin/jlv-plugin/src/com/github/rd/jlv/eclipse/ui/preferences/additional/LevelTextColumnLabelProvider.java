package com.github.rd.jlv.eclipse.ui.preferences.additional;

import org.eclipse.jface.viewers.OwnerDrawLabelProvider;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TableItem;

import com.github.rd.jlv.props.LoglistLevelColor;

public class LevelTextColumnLabelProvider extends OwnerDrawLabelProvider {

	@Override
	protected void measure(Event event, Object element) {
		// no code
	}

	@Override
	protected void paint(Event event, Object element) {
		TableItem item = (TableItem) event.item;
		LoglistLevelColor level = (LoglistLevelColor) item.getData();
		Rectangle bounds = item.getBounds(event.index);
		Point point = event.gc.stringExtent(level.getName());
		int xOffset = bounds.width / 2 - point.x / 2;
		int yOffset = bounds.height / 2 - point.y / 2;
		int x = xOffset > 0 ? bounds.x + xOffset : bounds.x;
		int y = yOffset > 0 ? bounds.y + yOffset : bounds.y;
		event.gc.drawText(level.getName(), x, y, true);
	}
}
