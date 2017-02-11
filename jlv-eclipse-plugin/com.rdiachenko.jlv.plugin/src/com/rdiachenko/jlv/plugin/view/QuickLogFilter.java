package com.rdiachenko.jlv.plugin.view;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.rdiachenko.jlv.Log;

public class QuickLogFilter extends ViewerFilter {

    private String searchText;

    public void setSearchText(String searchText) {
        StringBuilder builder = new StringBuilder();
        builder.append("(?i)(?s).*(").append(searchText).append(").*");
        this.searchText = builder.toString();
    }

    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        Log log = (Log) element;
        boolean passedFilter = searchText == null || searchText.isEmpty()
                || log.getDate().matches(searchText)
                || log.getLevel().matches(searchText)
                || log.getThreadName().matches(searchText)
                || log.getLoggerName().matches(searchText)
                || log.getMessage().matches(searchText)
                || log.getClassName().matches(searchText)
                || log.getMethodName().matches(searchText)
                || log.getLineNumber().matches(searchText)
                || log.getFileName().matches(searchText)
                || log.getMarker().matches(searchText)
                || log.getThrowable().matches(searchText)
                || log.getNdc().matches(searchText)
                || log.getMdc().matches(searchText);
        return passedFilter;
    }
}
