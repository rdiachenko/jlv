package com.github.rd.jlv.pfers;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class StructuralModel {

    @JsonProperty(value="items")
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

	    @JsonProperty(value="n")
		private String name;

		@JsonProperty(value="w")
		private int width;

		@JsonProperty(value="d")
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
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (display ? 1231 : 1237);
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + width;
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
			ModelItem other = (ModelItem) obj;
			if (display != other.display) {
				return false;
			}
			if (name == null) {
				if (other.name != null) {
					return false;
				}
			} else if (!name.equals(other.name)) {
				return false;
			}
			if (width != other.width) {
				return false;
			}
			return true;
		}

		@Override
		public String toString() {
			return "ModelItem [name=" + name + ", width=" + width + ", display=" + display + "]";
		}
	}
}
