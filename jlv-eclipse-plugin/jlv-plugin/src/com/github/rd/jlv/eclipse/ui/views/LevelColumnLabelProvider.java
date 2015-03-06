package com.github.rd.jlv.eclipse.ui.views;

import org.eclipse.jface.viewers.OwnerDrawLabelProvider;
import org.eclipse.swt.widgets.Event;

import com.github.rd.jlv.eclipse.JlvActivator;
import com.github.rd.jlv.eclipse.ResourceManager;
import com.github.rd.jlv.eclipse.ui.preferences.PreferenceManager;

public class LevelColumnLabelProvider extends OwnerDrawLabelProvider {

	private PreferenceManager preferenceManager;

	private ResourceManager resourceManager;

	public LevelColumnLabelProvider() {
		preferenceManager = JlvActivator.getDefault().getPreferenceManager();
		resourceManager = JlvActivator.getDefault().getResourceManager();
	}

	@Override
	public void measure(Event event, Object element) {
		// no code
	}

	@Override
	protected void paint(Event event, Object element) {
		// TODO Auto-generated method stub

	}

//	@Override
//	public void update(ViewerCell cell) {
//		Log log = (Log) cell.getElement();
//		cell.setBackground(getBackground(log));
//		cell.setForeground(getForeground(log));
//		cell.setFont(getFont(log));
//		super.update(cell);
//	}
//
//	@Override
//	public void paint(Event event, Object element) {
//		Log log = (Log) element;
//		Rectangle bounds = ((TableItem) event.item).getBounds(event.index);
//
//		if (preferenceManager.isLogLevelAsImage()) {
//			Image image = resourceManager.getImage(log.getLevel());
//			Rectangle imageBounds = image.getBounds();
//			int xOffset = bounds.width / 2 - imageBounds.width / 2;
//			int yOffset = bounds.height / 2 - imageBounds.height / 2;
//			int x = xOffset > 0 ? bounds.x + xOffset : bounds.x;
//			int y = yOffset > 0 ? bounds.y + yOffset : bounds.y;
//			event.gc.drawImage(image, x, y);
//		} else {
//			Point point = event.gc.stringExtent(log.getLevel());
//			int xOffset = bounds.width / 2 - point.x / 2;
//			int yOffset = bounds.height / 2 - point.y / 2;
//			int x = xOffset > 0 ? bounds.x + xOffset : bounds.x;
//			int y = yOffset > 0 ? bounds.y + yOffset : bounds.y;
//			event.gc.drawText(log.getLevel(), x, y, true);
//		}
//	}
//
//	@Override
//	public void erase(Event event, Object element) {
//		if ((event.detail & SWT.SELECTED) != 0) {
//			event.detail &= ~SWT.FOREGROUND;
//		}
//	}
//
//	private Color getBackground(Log log) {
//		Rgb rgb = preferenceManager.getLogLevelRgb(log.getLevel(), false);
//		return resourceManager.getColor(rgb);
//	}
//
//	private Color getForeground(Log log) {
//		Rgb rgb = preferenceManager.getLogLevelRgb(log.getLevel());
//		return resourceManager.getColor(rgb);
//	}
//
//	private Font getFont(Log log) {
//		return resourceManager.getFont(preferenceManager.getFontSize());
//	}
}
