package com.rdiachenko.jlv.plugin.preference;

import java.util.Objects;

import com.google.common.base.Preconditions;

public class StructuralModelItem {

  private final String fieldName;
  private boolean display;
  private int width;

  public StructuralModelItem(String fieldName, boolean display, int width) {
    Preconditions.checkNotNull(fieldName, "Field name is null");
    this.fieldName = fieldName;
    this.display = display;
    this.width = width;
  }

  public boolean isDisplay() {
    return display;
  }

  public void setDisplay(boolean display) {
    this.display = display;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public String getFieldName() {
    return fieldName;
  }

  @Override
  public int hashCode() {
    return Objects.hash(fieldName);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    StructuralModelItem item = (StructuralModelItem) other;
    return Objects.equals(fieldName, item.fieldName);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder(StructuralModelItem.class.getName());
    builder.append("[fieldName=").append(fieldName)
        .append(", display=").append(display)
        .append(", width=").append(width).append("]");
    return builder.toString();
  }
}
