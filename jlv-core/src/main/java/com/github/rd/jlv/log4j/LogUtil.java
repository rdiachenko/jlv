package com.github.rd.jlv.log4j;

import java.sql.Timestamp;
import java.util.Map;

import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

import com.github.rd.jlv.log4j.domain.Log;
import com.google.common.base.Strings;

public final class LogUtil {

	private LogUtil() {
		throw new IllegalStateException("This is an util class. The object should not be created.");
	}

	public static Log convert(LoggingEvent le) {
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

	public static String getValue(Log log, String logField) {
		switch (logField) {
		case LogConstants.LEVEL_FIELD_NAME:
			return log.getLevel();
		case LogConstants.CATEGORY_FIELD_NAME:
			return log.getCategoryName();
		case LogConstants.MESSAGE_FIELD_NAME:
			return log.getMessage();
		case LogConstants.LINE_FIELD_NAME:
			return log.getLineNumber();
		case LogConstants.DATE_FIELD_NAME:
			return log.getDate();
		case LogConstants.THROWABLE_FIELD_NAME:
			return log.getThrowable();
		case LogConstants.CLASS_FIELD_NAME:
			return log.getClassName();
		case LogConstants.FILE_FIELD_NAME:
			return log.getFileName();
		case LogConstants.LOCATION_INFO_FIELD_NAME:
			return log.getLocationInfo();
		case LogConstants.METHOD_FIELD_NAME:
			return log.getMethodName();
		case LogConstants.MILLISECONDS_FIELD_NAME:
			return log.getMs();
		case LogConstants.THREAD_FIELD_NAME:
			return log.getThreadName();
		case LogConstants.NDC_FIELD_NAME:
			return log.getNdc();
		case LogConstants.MDC_FIELD_NAME:
			return log.getMdc();
		default:
			throw new IllegalArgumentException("No log field with such name: " + logField);
		}
	}
}
