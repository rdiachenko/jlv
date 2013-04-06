package com.rdiachenko.jlv.log4j.domain;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.spi.LoggingEvent;

public final class LogContainer {

	private Set<LoggingEvent> logList;

	public static LogContainer createNewContainer() {
		return new LogContainer();
	}

	private LogContainer() {
		logList = new HashSet<LoggingEvent>();
	}

	public void add(LoggingEvent le) {
		logList.add(le);
	}

	public void remove(LoggingEvent le) {
		logList.remove(le);
	}

	public Iterator<LoggingEvent> iterator() {
		return logList.iterator();
	}

	public int size() {
		return logList.size();
	}

	@Override
	public String toString() {
		return logList.toString();
	}
}
