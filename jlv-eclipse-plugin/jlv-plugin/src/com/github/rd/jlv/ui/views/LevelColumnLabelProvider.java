package com.github.rd.jlv.ui.views;

import org.eclipse.jface.viewers.OwnerDrawLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TableItem;

import com.github.rd.jlv.JlvActivator;
import com.github.rd.jlv.ResourceUtils;
import com.github.rd.jlv.log4j.domain.Log;
import com.github.rd.jlv.pfers.PresentationalModel.ModelItem.Rgb;
import com.github.rd.jlv.ui.preferences.PreferenceManager;

public class LevelColumnLabelProvider extends OwnerDrawLabelProvider {

	private PreferenceManager preferenceManager;

	public LevelColumnLabelProvider() {
		this.preferenceManager = JlvActivator.getDefault().getPreferenceManager();
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

		if (preferenceManager.getDetailedPrefs().isLogLevelAsImage()) {
			Image image = ResourceUtils.getImage(log.getLevel());
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
		Rgb rgb = preferenceManager.getDetailedPrefs().getLogLevelRgb(log.getLevel(), false);
		return ResourceUtils.getColor(rgb);
	}

	private Color getForeground(Log log) {
		Rgb rgb = preferenceManager.getDetailedPrefs().getLogLevelRgb(log.getLevel());
		return ResourceUtils.getColor(rgb);
	}

	private Font getFont(Log log) {
		return ResourceUtils.getFont(preferenceManager.getDetailedPrefs().getFontSize());
	}
}
