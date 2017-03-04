package com.rdiachenko.jlv.converter;

import java.util.Map;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.ThrowableProxy;

import com.rdiachenko.jlv.Log;
import com.rdiachenko.jlv.LogUtils;

public class Log4j2Converter implements Converter {

  @Override
  public Log convert(Object obj) {
    LogEvent le = (LogEvent) obj;
    Log.Builder builder = Log.newBuilder()
        .date(LogUtils.formatDate(le.getTimeMillis()))
        .level(le.getLevel().toString())
        .threadName(LogUtils.nullToEmpty(le.getThreadName()))
        .loggerName(LogUtils.nullToEmpty(le.getLoggerName()))
        .ndc(le.getContextStack().toString())
        .mdc(getMdc(le));

    if (le.getMessage() != null) {
      builder.message(LogUtils.nullToEmpty(le.getMessage().getFormattedMessage()));
    }

    if (le.getMarker() != null) {
      builder.marker(le.getMarker().getName());
    }

    StackTraceElement element = le.getSource();

    if (element != null) {
      builder.className(LogUtils.nullToEmpty(element.getClassName()))
          .methodName(LogUtils.nullToEmpty(element.getMethodName()))
          .lineNumber(Integer.toString(element.getLineNumber()))
          .fileName(LogUtils.nullToEmpty(element.getFileName()));
    }

    ThrowableProxy throwable = le.getThrownProxy();

    if (throwable != null) {
      builder.throwable(throwable.getExtendedStackTraceAsString());
    }
    return builder.build();
  }

  private String getMdc(LogEvent le) {
    Map<String, String> mdcMap = le.getContextData().toMap();
    String mdc = "";

    if (mdcMap != null && !mdcMap.isEmpty()) {
      mdc = mdcMap.toString();
    }
    return mdc;
  }
}
