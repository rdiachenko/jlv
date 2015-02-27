package com.github.rd.jlv.eclipse.ui.views;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.github.rd.jlv.LogConstants;
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
//			Log log = (Log) element;
//
//			for (String logFieldName : LogConstants.LOG_FIELD_NAMES) {
//				if (LogUtils.getValue(log, logFieldName).matches(searchText)) {
//					isPastFilter = true;
//					break;
//				}
//			}
		}
		return isPastFilter;
	}
}