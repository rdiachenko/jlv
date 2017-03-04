package com.rdiachenko.jlv.plugin;

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
    boolean passedFilter = searchText == null || searchText.isEmpty();

    if (!passedFilter) {
      for (LogField field : LogField.values()) {
        passedFilter |= field.valueOf(log).matches(searchText);
      }
    }
    return passedFilter;
  }
}
