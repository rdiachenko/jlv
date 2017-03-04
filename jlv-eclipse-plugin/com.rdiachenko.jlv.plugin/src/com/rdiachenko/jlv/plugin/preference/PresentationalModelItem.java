package com.rdiachenko.jlv.plugin.preference;

import java.util.Objects;

import com.google.common.base.Preconditions;

public class PresentationalModelItem {

  private final String levelName;
  private Rgb foreground;
  private Rgb background;

  public PresentationalModelItem(String levelName, Rgb foreground, Rgb background) {
    Preconditions.checkNotNull(levelName, "Level name is null");
    Preconditions.checkNotNull(foreground, "Foreground is null");
    Preconditions.checkNotNull(background, "Background is null");
    this.levelName = levelName;
    this.foreground = foreground;
    this.background = background;
  }

  public String getLevelName() {
    return levelName;
  }

  public Rgb getForeground() {
    return foreground;
  }

  public void setForeground(Rgb foreground) {
    Preconditions.checkNotNull(foreground, "Foreground is null");
    this.foreground = foreground;
  }

  public Rgb getBackground() {
    return background;
  }

  public void setBackground(Rgb background) {
    Preconditions.checkNotNull(background, "Background is null");
    this.background = background;
  }

  @Override
  public int hashCode() {
    return Objects.hash(levelName);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    PresentationalModelItem item = (PresentationalModelItem) other;
    return Objects.equals(levelName, item.levelName);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder(PresentationalModelItem.class.getName());
    builder.append("[levelName=").append(levelName)
        .append(", foreground=").append(foreground)
        .append(", background=").append(background).append("]");
    return builder.toString();
  }
}
