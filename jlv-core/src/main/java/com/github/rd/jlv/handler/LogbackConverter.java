package com.github.rd.jlv.handler;

import ch.qos.logback.classic.spi.LoggingEventVO;

import com.github.rd.jlv.Log;
import com.google.common.base.Strings;

public class LogbackConverter implements LogConverter {

	@Override
	public Log convert(Object value) {
		LoggingEventVO log = (LoggingEventVO) value;

		Log.Builder builder = new Log.Builder();
		builder.categoryName(Strings.nullToEmpty(log.getLoggerName()));
		builder.threadName(Strings.nullToEmpty(log.getThreadName()));
		builder.message(Strings.nullToEmpty(log.getMessage()));
		builder.level(Strings.nullToEmpty(log.getLevel().toString()));

		return builder.build();
	}
}
