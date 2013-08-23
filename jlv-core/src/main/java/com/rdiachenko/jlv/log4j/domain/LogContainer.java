package com.rdiachenko.jlv.log4j.domain;

import java.util.Iterator;

import org.apache.commons.collections.buffer.CircularFifoBuffer;

public final class LogContainer {

	// Generics for CircularFifoBuffer will be provided in commons-collections v.4.0
	// TODO: use replace current 3.2.1 commons-collections version to 4.0 when it releases
	private final CircularFifoBuffer container;

	public LogContainer(int bufferSize) {
		container = new CircularFifoBuffer(bufferSize);
	}

	public int size() {
		return container.size();
	}

	public void add(Log log) {
		container.add(log);
	}

	public Log get() {
		return (Log) container.get();
	}

	public Iterator<Log> iterator() {
		return container.iterator();
	}

	public void clear() {
		container.clear();
	}
}
