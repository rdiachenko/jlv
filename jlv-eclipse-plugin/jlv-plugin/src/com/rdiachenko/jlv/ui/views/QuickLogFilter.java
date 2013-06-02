package com.rdiachenko.jlv.ui.views;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.google.common.base.Strings;
import com.rdiachenko.jlv.log4j.domain.Log;
import com.rdiachenko.jlv.model.LogField;

public class QuickLogFilter extends ViewerFilter {

	private String searchText;

	public void setSearchText(String searchText) {
		StringBuilder builder = new StringBuilder();
		builder.append(".*(").append(searchText.toLowerCase()).append("|")
				.append(searchText.toUpperCase()).append(").*");
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
