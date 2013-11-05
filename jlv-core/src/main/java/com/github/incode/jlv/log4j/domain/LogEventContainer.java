package com.github.incode.jlv.log4j.domain;

import java.util.HashSet;
import java.util.Set;

public final class LogEventContainer {

	private static final Set<LogEventListener> LISTENERS = new HashSet<LogEventListener>();

	private LogEventContainer() {
		throw new IllegalStateException("This is an util class. The object should not be created.");
	}

	public static void notifyListeners(Log logEvent) {
		for (LogEventListener listener : LISTENERS) {
			listener.handleLogEvent(logEvent);
		}
	}

	public static void addListener(final LogEventListener listener) {
		if (listener != null) {
			LISTENERS.add(listener);
		}
	}

	public static void removeListener(final LogEventListener listener) {
		LISTENERS.remove(listener);
	}
}
