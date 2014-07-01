package com.github.rd.jlv.prefs;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class PresentationalModel {

	@JsonProperty(value = "img")
	private boolean levelAsImage;

	@JsonProperty(value = "font")
	private int fontSize;

	@JsonProperty(value = "items")
	private List<ModelItem> modelItems;

	public boolean isLevelAsImage() {
		return levelAsImage;
	}

	public void setLevelAsImage(boolean levelAsImage) {
		this.levelAsImage = levelAsImage;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public List<ModelItem> getModelItems() {
		return modelItems;
	}

	public void setModelItems(List<ModelItem> modelItems) {
		this.modelItems = modelItems;
	}

	@Override
	public String toString() {
		return "PresentationalModel [levelAsImage=" + levelAsImage + ", fontSize=" + fontSize + ", modelItems="
				+ modelItems + "]";
	}

	public static class ModelItem {

		@JsonProperty(value = "level")
		private String levelName;

		@JsonProperty(value = "fcolor")
		private Rgb foreground;

		@JsonProperty(value = "bcolor")
		private Rgb background;

		public String getLevelName() {
			return levelName;
		}

		public void setLevelName(String levelName) {
			this.levelName = levelName;
		}

		public Rgb getForeground() {
			return foreground;
		}

		public void setForeground(Rgb foreground) {
			this.foreground = foreground;
		}

		public Rgb getBackground() {
			return background;
		}

		public void setBackground(Rgb background) {
			this.background = background;
		}

		@Override
		public String toString() {
			return "ModelItem [levelName=" + levelName + ", foreground=" + foreground + ", background=" + background
					+ "]";
		}

		public static class Rgb {

			@JsonProperty(value = "r")
			private int red;

			@JsonProperty(value = "g")
			private int green;

			@JsonProperty(value = "b")
			private int blue;

			public Rgb(int red, int green, int blue) {
				this.red = red;
				this.green = green;
				this.blue = blue;
			}

			public Rgb() {
				// no code
			}

			public int getRed() {
				return red;
			}

			public void setRed(int red) {
				this.red = red;
			}

			public int getGreen() {
				return green;
			}

			public void setGreen(int green) {
				this.green = green;
			}

			public int getBlue() {
				return blue;
			}

			public void setBlue(int blue) {
				this.blue = blue;
			}

			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result + blue;
				result = prime * result + green;
				result = prime * result + red;
				return result;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj) {
					return true;
				}
				if (obj == null) {
					return false;
				}
				if (getClass() != obj.getClass()) {
					return false;
				}
				Rgb other = (Rgb) obj;
				if (blue != other.blue) {
					return false;
				}
				if (green != other.green) {
					return false;
				}
				if (red != other.red) {
					return false;
				}
				return true;
			}

			@Override
			public String toString() {
				return "Rgb [red=" + red + ", green=" + green + ", blue=" + blue + "]";
			}
		}
	}
}
