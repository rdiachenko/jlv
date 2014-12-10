package com.github.rd.jlv;

import java.sql.Timestamp;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

import ch.qos.logback.classic.spi.LoggingEventVO;

import com.google.common.base.Strings;

public final class LogUtils {

	private LogUtils() {
		throw new IllegalStateException("This is an util class. The object should not be created.");
	}
	
	public static Log convert(Object le) {
		if (le instanceof LoggingEvent) {
			return convert((LoggingEvent) le);
		} else if (le instanceof LoggingEventVO) {
			return convert((LoggingEventVO) le);
		} else {
			throw new IllegalArgumentException("No handler for such a log event: " + le);
		}
	}

	private static Log convert(LoggingEvent le) {
		Log.Builder builder = new Log.Builder();
		builder.categoryName(Strings.nullToEmpty(le.getLoggerName()));
		builder.threadName(Strings.nullToEmpty(le.getThreadName()));
		builder.message(Strings.nullToEmpty(le.getRenderedMessage()));
		builder.level(Strings.nullToEmpty(le.getLevel().toString()));
		builder.ndc(Strings.nullToEmpty(le.getNDC()));
		LocationInfo localInfo = le.getLocationInformation();

		if (localInfo != null) {
			builder.className(Strings.nullToEmpty(localInfo.getClassName()));
			builder.fileName(Strings.nullToEmpty(localInfo.getFileName()));
			builder.lineNumber(Strings.nullToEmpty(localInfo.getLineNumber()));
			builder.methodName(Strings.nullToEmpty(localInfo.getMethodName()));
			builder.locationInfo(Strings.nullToEmpty(localInfo.fullInfo));
		}
		ThrowableInformation throwable = le.getThrowableInformation();

		if (throwable != null && throwable.getThrowableStrRep() != null) {
			StringBuilder strBuilder = new StringBuilder();
			String lineSeparator = "\n";

			for (String exception : throwable.getThrowableStrRep()) {
				strBuilder.append(exception).append(lineSeparator);
			}
			builder.throwable(Strings.nullToEmpty(strBuilder.toString()));
		}
		long time = le.getTimeStamp();

		if (time > 0) {
			builder.ms(String.valueOf(time));
			builder.date(new Timestamp(time).toString());
		}
		Map<?, ?> mdc = le.getProperties();

		if (mdc != null && !mdc.isEmpty()) {
			builder.mdc(mdc.toString());
		} else {
			builder.mdc("");
		}
		Log log = builder.build();
		return log;
	}
	
	private static Log convert(LoggingEventVO le) {
		Log.Builder builder = new Log.Builder();
		builder.categoryName(Strings.nullToEmpty(le.getLoggerName()));
		builder.threadName(Strings.nullToEmpty(le.getThreadName()));
		builder.message(Strings.nullToEmpty(le.getMessage()));
		builder.level(Strings.nullToEmpty(le.getLevel().toString()));
		
		if (le.hasCallerData()) {
			StackTraceElement[] localInfo = le.getCallerData();
		}
		Log log = builder.build();
		return log;
	}

	public static Log convert(String log, Pattern pattern) {
		return null; // TODO: implement
	}
}
