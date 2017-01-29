package com.rdiachenko.jlv.converter;

import java.util.Map;

import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

import com.rdiachenko.jlv.Log;
import com.rdiachenko.jlv.LogUtils;

public class Log4j1Converter implements Converter<LoggingEvent> {
  
  @Override
  public Log convert(LoggingEvent le) {
    Log.Builder builder = Log.newBuilder()
        .date(LogUtils.formatDate(le.getTimeStamp()))
        .level(le.getLevel().toString())
        .threadName(LogUtils.nullToEmpty(le.getThreadName()))
        .loggerName(LogUtils.nullToEmpty(le.getLoggerName()))
        .message(LogUtils.nullToEmpty(le.getRenderedMessage()))
        .throwable(getThrowable(le))
        .ndc(LogUtils.nullToEmpty(le.getNDC()))
        .mdc(getMdc(le));
    
    LocationInfo localInfo = le.getLocationInformation();
    
    if (localInfo != null) {
      builder.className(LogUtils.nullToEmpty(localInfo.getClassName()))
          .methodName(LogUtils.nullToEmpty(localInfo.getMethodName()))
          .lineNumber(LogUtils.nullToEmpty(localInfo.getLineNumber()))
          .fileName(LogUtils.nullToEmpty(localInfo.getFileName()));
    }
    return builder.build();
  }
  
  private String getThrowable(LoggingEvent le) {
    ThrowableInformation throwableInfo = le.getThrowableInformation();
    StringBuilder throwable = new StringBuilder();
    
    if (throwableInfo != null && throwableInfo.getThrowableStrRep() != null) {
      for (String exception : throwableInfo.getThrowableStrRep()) {
        throwable.append(exception).append(System.lineSeparator());
      }
    }
    return throwable.toString().trim();
  }
  
  private String getMdc(LoggingEvent le) {
    Map<?, ?> mdcMap = le.getProperties();
    String mdc = "";
    
    if (mdcMap != null && !mdcMap.isEmpty()) {
      mdc = mdcMap.toString();
    }
    return mdc;
  }
}
