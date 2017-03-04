package com.rdiachenko.jlv.plugin.view;

import org.eclipse.jface.viewers.TableViewer;

import com.rdiachenko.jlv.Log;
import com.rdiachenko.jlv.plugin.LogField;

public class LimitedTextColumnLabelProvider extends DefaultColumnLabelProvider {

  private static final int TEXT_LENGTH_LIMIT = 200;

  public LimitedTextColumnLabelProvider(TableViewer viewer, LogField field) {
    super(viewer, field);
  }

  @Override
  public String getText(Object element) {
    Log log = (Log) element;
    String value = field.valueOf(log).replaceAll("\\s+", " ");

    if (value.length() > TEXT_LENGTH_LIMIT) {
      value = value.substring(0, TEXT_LENGTH_LIMIT) + "...";
    }
    return value;
  }
}
