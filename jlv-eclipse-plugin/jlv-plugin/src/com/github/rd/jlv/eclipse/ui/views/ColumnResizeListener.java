package com.github.rd.jlv.eclipse.ui.views;

import java.util.List;

import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.widgets.TableColumn;

import com.github.rd.jlv.eclipse.JlvActivator;
import com.github.rd.jlv.props.JlvProperties;
import com.github.rd.jlv.props.LoglistColumn;
import com.github.rd.jlv.props.PropertyKey;

public class ColumnResizeListener implements ControlListener {

	private static final int DELAY = 500; // ms

	private long lastEventTime;
	
	public ColumnResizeListener() {
		lastEventTime = System.currentTimeMillis();
	}

	@Override
	public void controlMoved(ControlEvent e) {
		// no code
	}

	@Override
	public void controlResized(ControlEvent event) {
		if (event.getSource() instanceof TableColumn) {
			long currentEventTime = System.currentTimeMillis();
			
			if ((lastEventTime + DELAY) < currentEventTime) {
				TableColumn column = (TableColumn) event.getSource();
				String columnName = column.getText();
				int width = column.getWidth();
				store(columnName, width);
				lastEventTime = currentEventTime;
			}
		}
	}
	
	private void store(String columnName, int width) {
		JlvProperties store = JlvActivator.getDefault().getStore();
		List<LoglistColumn> columns = store.load(PropertyKey.LOGLIST_COLUMN_KEY);
		
		for (LoglistColumn column : columns) {
			if (column.equals(columnName)) {
				column.setWidth(width);
				break;
			}
		}
		store.save(PropertyKey.LOGLIST_COLUMN_KEY, columns);
	}
}
