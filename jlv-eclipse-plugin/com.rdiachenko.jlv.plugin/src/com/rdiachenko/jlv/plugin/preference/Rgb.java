package com.rdiachenko.jlv.plugin.preference;

import java.util.Objects;

public class Rgb {

  private final int red;
  private final int green;
  private final int blue;

  public Rgb(int red, int green, int blue) {
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  public int getRed() {
    return red;
  }

  public int getGreen() {
    return green;
  }

  public int getBlue() {
    return blue;
  }

  @Override
  public int hashCode() {
    return Objects.hash(red, green, blue);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    Rgb rgb = (Rgb) other;
    return Objects.equals(red, rgb.red)
        && Objects.equals(green, rgb.green)
        && Objects.equals(blue, rgb.blue);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder(Rgb.class.getName());
    builder.append("[red=").append(red)
        .append(", green=").append(green)
        .append(", blue=").append(blue).append("]");
    return builder.toString();
  }
}
