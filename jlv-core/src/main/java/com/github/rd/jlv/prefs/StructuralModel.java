package com.github.rd.jlv.prefs;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class StructuralModel {

	@JsonProperty(value = "items")
	private List<ModelItem> modelItems;

	public List<ModelItem> getModelItems() {
		return modelItems;
	}

	public void setModelItems(List<ModelItem> modelItems) {
		this.modelItems = modelItems;
	}

	@Override
	public String toString() {
		return "StructuralModel [modelItems=" + modelItems + "]";
	}

	public static class ModelItem {

		@JsonProperty(value = "n")
		private String name;

		@JsonProperty(value = "w")
		private int width;

		@JsonProperty(value = "d")
		private boolean display;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public boolean isDisplay() {
			return display;
		}

		public void setDisplay(boolean display) {
			this.display = display;
		}

		@Override
		public String toString() {
			return "ModelItem [name=" + name + ", width=" + width + ", display=" + display + "]";
		}
	}
}
