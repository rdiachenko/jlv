package com.github.rd.jlv.prefs;

import org.codehaus.jackson.annotate.JsonProperty;

public class GeneralModel implements Model {

	@JsonProperty(value = "port")
	private int portNumber;

	@JsonProperty(value = "astart")
	private boolean autoStart;

	@JsonProperty(value = "qsearch")
	private boolean quickSearch;

	@JsonProperty(value = "bsize")
	private int bufferSize;

	@JsonProperty(value = "rtime")
	private int refreshingTime;

	public GeneralModel() {
		// no code
	}

	public GeneralModel(GeneralModel model) {
		portNumber = model.getPortNumber();
		autoStart = model.isAutoStart();
		quickSearch = model.isQuickSearch();
		bufferSize = model.getBufferSize();
		refreshingTime = model.getRefreshingTime();
	}

	public int getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	public boolean isAutoStart() {
		return autoStart;
	}

	public void setAutoStart(boolean autoStart) {
		this.autoStart = autoStart;
	}

	public boolean isQuickSearch() {
		return quickSearch;
	}

	public void setQuickSearch(boolean quickSearch) {
		this.quickSearch = quickSearch;
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public int getRefreshingTime() {
		return refreshingTime;
	}

	public void setRefreshingTime(int refreshingTime) {
		this.refreshingTime = refreshingTime;
	}

	@Override
	public String toString() {
		return "GeneralModel [portNumber=" + portNumber + ", autoStart=" + autoStart + ", quickSearch=" + quickSearch
				+ ", bufferSize=" + bufferSize
				+ ", refreshingTime=" + refreshingTime + "]";
	}
}
