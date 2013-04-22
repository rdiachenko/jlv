package com.rdiachenko.jlv.log4j.domain;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class LogContainer {

	private final List<Log> container;

	public LogContainer() {
		container = new LinkedList<Log>();
	}

	public int size() {
		return container.size();
	}

	public void add(Log log) {
		container.add(log);
	}

	public Log get(int index) {
		return container.get(index);
	}

	public Iterator<Log> iterator() {
		return container.iterator();
	}
}
