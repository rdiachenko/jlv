package com.github.rd.jlv.prefs;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class StructuralModel implements Model {

	@JsonProperty(value = "items")
	private List<ModelItem> modelItems;

	public StructuralModel() {
		// no code
	}

	public StructuralModel(StructuralModel model) {
		modelItems = new ArrayList<>();

		for (ModelItem item : model.getModelItems()) {
			modelItems.add(new ModelItem(item));
		}
	}

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

		public ModelItem() {
			// no code
		}

		public ModelItem(ModelItem item) {
			name = item.getName();
			width = item.getWidth();
			display = item.isDisplay();
		}

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
