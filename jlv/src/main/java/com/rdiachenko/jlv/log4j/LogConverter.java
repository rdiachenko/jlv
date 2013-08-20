package com.rdiachenko.jlv.log4j;

import org.apache.log4j.spi.LoggingEvent;

import com.rdiachenko.jlv.log4j.domain.Log;

public final class LogConverter {

	private LogConverter() {
		throw new IllegalStateException("This is an util class. The object should not be created.");
	}

	public static Log convert(LoggingEvent le) {
		return null;
	}
}
