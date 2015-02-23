package com.github.rd.jlv.handler;

import java.sql.Timestamp;
import java.util.Map;

import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

import com.github.rd.jlv.Log;
import com.google.common.base.Strings;

public class Log4j1Converter implements LogConverter {

	@Override
	public Log convert(Object value) {
		LoggingEvent log = (LoggingEvent) value;

		Log.Builder builder = new Log.Builder();
		builder.categoryName(Strings.nullToEmpty(log.getLoggerName()));
		builder.threadName(Strings.nullToEmpty(log.getThreadName()));
		builder.message(Strings.nullToEmpty(log.getRenderedMessage()));
		builder.level(Strings.nullToEmpty(log.getLevel().toString()));
		builder.ndc(Strings.nullToEmpty(log.getNDC()));

		fillLocationInfo(builder, log);
		fillThrowable(builder, log);
		fillTimestamp(builder, log);
		fillMdc(builder, log);

		return builder.build();
	}

	private void fillLocationInfo(Log.Builder builder, LoggingEvent log) {
		LocationInfo localInfo = log.getLocationInformation();

		if (localInfo != null) {
			builder.className(Strings.nullToEmpty(localInfo.getClassName()));
			builder.fileName(Strings.nullToEmpty(localInfo.getFileName()));
			builder.lineNumber(Strings.nullToEmpty(localInfo.getLineNumber()));
			builder.methodName(Strings.nullToEmpty(localInfo.getMethodName()));
			builder.locationInfo(Strings.nullToEmpty(localInfo.fullInfo));
		}
	}

	private void fillThrowable(Log.Builder builder, LoggingEvent log) {
		ThrowableInformation throwable = log.getThrowableInformation();

		if (throwable != null && throwable.getThrowableStrRep() != null) {
			StringBuilder strBuilder = new StringBuilder();
			String lineSeparator = "\n";

			for (String exception : throwable.getThrowableStrRep()) {
				strBuilder.append(exception).append(lineSeparator);
			}
			builder.throwable(Strings.nullToEmpty(strBuilder.toString()));
		}
	}

	private void fillTimestamp(Log.Builder builder, LoggingEvent log) {
		long time = log.getTimeStamp();

		if (time > 0) {
			builder.ms(String.valueOf(time));
			builder.date(new Timestamp(time).toString());
		}
	}

	private void fillMdc(Log.Builder builder, LoggingEvent log) {
		Map<?, ?> mdc = log.getProperties();

		if (mdc != null && !mdc.isEmpty()) {
			builder.mdc(mdc.toString());
		} else {
			builder.mdc("");
		}
	}
}
