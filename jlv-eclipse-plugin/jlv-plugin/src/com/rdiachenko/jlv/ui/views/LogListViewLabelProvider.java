package com.rdiachenko.jlv.ui.views;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.rdiachenko.jlv.log4j.domain.Log;
import com.rdiachenko.jlv.model.LogField;

public class LogListViewLabelProvider extends LabelProvider implements ITableLabelProvider {

//	To enhance the Favorites view with different fonts and colors, implement
//	IFontProvider and IColorProvider respectively (see Section 13.2.5, ICol-
//	orProvider, on page 523)

	private static final int TEXT_LENGTH_LIMIT = 200;

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		Log log = (Log) element;
		LogField logField = LogField.getLogFieldByIndex(columnIndex);
		String columnText = logField.getValue(log);

		if (logField == LogField.MESSAGE || logField == LogField.THROWABLE) {
			columnText = columnText.replaceAll("\\r|\\n", " ");

			if (columnText.length() > TEXT_LENGTH_LIMIT) {
				columnText = columnText.substring(0, TEXT_LENGTH_LIMIT) + "...";
			}
		}

		return columnText;
	}
}
