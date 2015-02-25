package com.github.rd.jlv.handler;

import org.apache.log4j.spi.LoggingEvent;

import ch.qos.logback.classic.spi.ILoggingEvent;

public enum LogType {

	LOG4J1(new Log4j1Converter()),
	LOGBACK(new LogbackConverter());

	private final LogConverter logConverter;

	private LogType(LogConverter logConverter) {
		this.logConverter = logConverter;
	}

	public LogConverter converter() {
		return logConverter;
	}

	public static LogType typeOf(Object value) {
		if (value instanceof LoggingEvent) {
			return LOG4J1;
		} else if (value instanceof ILoggingEvent) {
			return LOGBACK;
		} else {
			throw new IllegalArgumentException("No log type for such a value: " + value);
		}
	}
}
