package com.rdiachenko.jlv;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TimeZone;

import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.ThrowableInformation;

public final class LogConverter {

  public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

  private LogConverter() {
    // utility class
  }

  public static Log convert(org.apache.log4j.spi.LoggingEvent le) {
    Log.Builder builder = Log.newBuilder()
        .date(formatDate(le.getTimeStamp()))
        .level(le.getLevel().toString())
        .threadName(nullToEmpty(le.getThreadName()))
        .loggerName(nullToEmpty(le.getLoggerName()))
        .message(nullToEmpty(le.getRenderedMessage()))
        .ndc(nullToEmpty(le.getNDC()));
    LocationInfo localInfo = le.getLocationInformation();

    if (localInfo != null) {
      builder.className(nullToEmpty(localInfo.getClassName()))
          .methodName(nullToEmpty(localInfo.getMethodName()))
          .lineNumber(nullToEmpty(localInfo.getLineNumber()))
          .fileName(nullToEmpty(localInfo.getFileName()));
    }
    ThrowableInformation throwable = le.getThrowableInformation();
    
    if (throwable != null && throwable.getThrowableStrRep() != null) {
      StringBuilder exBuilder = new StringBuilder();
      
      for (String exception : throwable.getThrowableStrRep()) {
        exBuilder.append(exception).append(System.lineSeparator());
      }
      builder.throwable(exBuilder.toString().trim());
    }
    Map<?, ?> mdc = le.getProperties();
    
    if (!mdc.isEmpty()) {
      builder.mdc(mdc.toString());
    }
    return builder.build();
  }

  public static Log convert(ch.qos.logback.classic.spi.LoggingEventVO le) {
    Log.Builder builder = Log.newBuilder();
    return builder.build();
  }

  public static Log convert(org.apache.logging.log4j.core.LogEvent le) {
    Log.Builder builder = Log.newBuilder();
    return builder.build();
  }
  
  public static String nullToEmpty(String val) {
    return val == null ? "" : val;
  }
  
  public static String formatDate(long timestamp) {
    LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), TimeZone
        .getDefault().toZoneId());
    return dateTime.format(DATE_FORMAT);
  }
}
