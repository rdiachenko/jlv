package com.rdiachenko.jlv;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TimeZone;

import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.ThrowableInformation;

import ch.qos.logback.classic.spi.ClassPackagingData;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;

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

    if (mdc != null && !mdc.isEmpty()) {
      builder.mdc(mdc.toString());
    }
    return builder.build();
  }
  
  public static Log convert(ch.qos.logback.classic.spi.LoggingEventVO le) {
    Log.Builder builder = Log.newBuilder()
        .date(formatDate(le.getTimeStamp()))
        .level(le.getLevel().toString())
        .threadName(nullToEmpty(le.getThreadName()))
        .loggerName(nullToEmpty(le.getLoggerName()))
        .message(nullToEmpty(le.getFormattedMessage()));
    
    if (le.getMarker() != null) {
      builder.marker(le.getMarker().getName());
    }

    StackTraceElement[] callerData = le.getCallerData();
    
    if (callerData != null && callerData.length > 0) {
      builder.className(nullToEmpty(callerData[0].getClassName()))
          .methodName(nullToEmpty(callerData[0].getMethodName()))
          .lineNumber(Integer.toString(callerData[0].getLineNumber()))
          .fileName(nullToEmpty(callerData[0].getFileName()));
    }

    IThrowableProxy throwable = le.getThrowableProxy();
    StringBuilder exBuilder = new StringBuilder();
    
    while (throwable != null) {
      exBuilder.append(throwable.getClassName())
          .append(": ")
          .append(throwable.getMessage())
          .append(System.lineSeparator());
      
      StackTraceElementProxy[] stackTrace = throwable.getStackTraceElementProxyArray();

      for (StackTraceElementProxy step : stackTrace) {
        exBuilder.append("\t").append(step);
        ClassPackagingData cpd = step.getClassPackagingData();

        if (cpd != null) {
          if (cpd.isExact()) {
            exBuilder.append(" [");
          } else {
            exBuilder.append(" ~[");
          }
          exBuilder.append(cpd.getCodeLocation()).append(':').append(cpd.getVersion()).append(']');
        }
        exBuilder.append(System.lineSeparator());
      }
      throwable = throwable.getCause();
    }
    builder.throwable(exBuilder.toString().trim());
    
    Map<String, String> mdc = le.getMDCPropertyMap();

    if (mdc != null && !mdc.isEmpty()) {
      builder.mdc(mdc.toString());
    }
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
