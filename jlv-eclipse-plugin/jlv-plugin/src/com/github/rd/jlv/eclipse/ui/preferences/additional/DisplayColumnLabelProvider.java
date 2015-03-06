package com.github.rd.jlv.eclipse.ui.preferences.additional;

import org.eclipse.jface.viewers.OwnerDrawLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TableItem;

import com.github.rd.jlv.eclipse.ImageType;
import com.github.rd.jlv.eclipse.JlvActivator;
import com.github.rd.jlv.eclipse.ResourceManager;
import com.github.rd.jlv.props.LoglistColumn;

public class DisplayColumnLabelProvider extends OwnerDrawLabelProvider {

	private final ResourceManager resourceManager = JlvActivator.getDefault().getResourceManager();

	@Override
	protected void measure(Event event, Object element) {
		// no code
	}

	@Override
	protected void paint(Event event, Object element) {
		TableItem item = (TableItem) event.item;
		LoglistColumn column = (LoglistColumn) item.getData();
		Image image = (column.isVisible()) ? resourceManager.getImage(ImageType.CHECKBOX_CHECKED_ICON)
				: resourceManager.getImage(ImageType.CHECKBOX_UNCHECKED_ICON);
		Rectangle bounds = item.getBounds(event.index);
		Rectangle imageBounds = image.getBounds();
		int xOffset = bounds.width / 2 - imageBounds.width / 2;
		int yOffset = bounds.height / 2 - imageBounds.height / 2;
		int x = xOffset > 0 ? bounds.x + xOffset : bounds.x;
		int y = yOffset > 0 ? bounds.y + yOffset : bounds.y;
		event.gc.drawImage(image, x, y);
	}
}
