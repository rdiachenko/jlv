package com.rdiachenko.jlv.plugin.view;

import org.eclipse.jface.viewers.OwnerDrawLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TableItem;

import com.rdiachenko.jlv.Log;
import com.rdiachenko.jlv.plugin.LogLevel;
import com.rdiachenko.jlv.plugin.PreferenceStoreUtils;
import com.rdiachenko.jlv.plugin.ResourceManager;
import com.rdiachenko.jlv.plugin.preference.PresentationalModel;
import com.rdiachenko.jlv.plugin.preference.PresentationalModelItem;

public class LevelColumnLabelProvider extends OwnerDrawLabelProvider {

  private final TableViewer viewer;

  public LevelColumnLabelProvider(TableViewer viewer) {
    this.viewer = viewer;
  }

  @Override
  public void measure(Event event, Object element) {
    // no code
  }

  @Override
  public void update(ViewerCell cell) {
    Log log = (Log) cell.getElement();
    PresentationalModel model = PreferenceStoreUtils.getPresentationalModel();
    PresentationalModelItem item = model.getModelItem(LogLevel.toLogLevel(log.getLevel()));
    cell.setBackground(ResourceManager.getColor(Display.getCurrent(), item.getBackground()));
    cell.setForeground(ResourceManager.getColor(Display.getCurrent(), item.getForeground()));
    cell.setFont(ResourceManager.getFont(viewer.getTable(), model.getFontSize(), SWT.NONE));
    super.update(cell);
  }

  @Override
  public void paint(Event event, Object element) {
    Log log = (Log) element;
    Rectangle bounds = ((TableItem) event.item).getBounds(event.index);
    PresentationalModel model = PreferenceStoreUtils.getPresentationalModel();

    if (model.isLevelAsImage()) {
      Image image = ResourceManager.getImage(LogLevel.toLogLevel(log.getLevel()));
      Rectangle imageBounds = image.getBounds();
      int xOffset = bounds.width / 2 - imageBounds.width / 2;
      int yOffset = bounds.height / 2 - imageBounds.height / 2;
      int x = xOffset > 0 ? event.x + xOffset : event.x;
      int y = yOffset > 0 ? event.y + yOffset : event.y;
      event.gc.drawImage(image, x, y);
    } else {
      Point point = event.gc.stringExtent(log.getLevel());
      int xOffset = bounds.width / 2 - point.x / 2;
      int yOffset = bounds.height / 2 - point.y / 2;
      int x = xOffset > 0 ? event.x + xOffset : event.x;
      int y = yOffset > 0 ? event.y + yOffset : event.y;
      event.gc.drawText(log.getLevel(), x, y, true);
    }
  }
}
