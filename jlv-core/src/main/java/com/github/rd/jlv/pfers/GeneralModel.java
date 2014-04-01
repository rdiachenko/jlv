package com.github.rd.jlv.pfers;

import org.codehaus.jackson.annotate.JsonProperty;

public class GeneralModel {

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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (autoStart ? 1231 : 1237);
        result = prime * result + bufferSize;
        result = prime * result + portNumber;
        result = prime * result + (quickSearch ? 1231 : 1237);
        result = prime * result + refreshingTime;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GeneralModel other = (GeneralModel) obj;
        if (autoStart != other.autoStart)
            return false;
        if (bufferSize != other.bufferSize)
            return false;
        if (portNumber != other.portNumber)
            return false;
        if (quickSearch != other.quickSearch)
            return false;
        if (refreshingTime != other.refreshingTime)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "GeneralModel [portNumber=" + portNumber + ", autoStart=" + autoStart + ", quickSearch=" + quickSearch + ", bufferSize=" + bufferSize
               + ", refreshingTime=" + refreshingTime + "]";
    }
}
