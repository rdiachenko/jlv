package com.rdiachenko.jlv.log4j;

import java.sql.Timestamp;

import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

import com.google.common.base.Strings;
import com.rdiachenko.jlv.log4j.domain.Log;

public final class LogConverter {

	private LogConverter() {
		throw new IllegalStateException("This is an util class. The object should not be created.");
	}

	public static Log convert(LoggingEvent le) {
		Log.Builder builder = new Log.Builder();
		builder.categoryName(Strings.nullToEmpty(le.getLoggerName()));
		builder.threadName(Strings.nullToEmpty(le.getThreadName()));
		builder.message(Strings.nullToEmpty(le.getRenderedMessage()));
		builder.level(Strings.nullToEmpty(le.getLevel().toString()));

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
		} else {
			builder.throwable("");
		}

		long time = le.getTimeStamp();

		if (time == 0) {
			builder.date("");
			builder.ms("");
		} else {
			builder.ms(String.valueOf(time));
			builder.date(new Timestamp(time).toString());
		}

		Log log = builder.build();
		return log;
	}
}
