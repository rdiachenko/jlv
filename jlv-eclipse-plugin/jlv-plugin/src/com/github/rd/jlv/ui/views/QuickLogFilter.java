package com.github.rd.jlv.ui.views;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.github.rd.jlv.log4j.domain.Log;
import com.github.rd.jlv.ui.LogField;
import com.google.common.base.Strings;

public class QuickLogFilter extends ViewerFilter {

	private String searchText;

	public void setSearchText(String searchText) {
		StringBuilder builder = new StringBuilder();
		builder.append("(?i)(?s).*(").append(searchText).append(").*");
		this.searchText = builder.toString();
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		boolean isPastFilter = false;

		if (Strings.isNullOrEmpty(searchText)) {
			isPastFilter = true;
		} else {
			Log log = (Log) element;

			for (LogField field : LogField.values()) {
				String value = field.getValue(log);

				if (value.matches(searchText)) {
					isPastFilter = true;
					break;
				}
			}
		}

		return isPastFilter;
	}
}
