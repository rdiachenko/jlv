package com.rdiachenko.jlv.converter;

import java.util.Map;

import com.rdiachenko.jlv.Log;
import com.rdiachenko.jlv.LogUtils;

import ch.qos.logback.classic.spi.ClassPackagingData;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;

public class LogbackConverter implements Converter {

    @Override
    public Log convert(Object obj) {
        ILoggingEvent le = (ILoggingEvent) obj;
        Log.Builder builder = Log.newBuilder()
                .date(LogUtils.formatDate(le.getTimeStamp()))
                .level(le.getLevel().toString())
                .threadName(LogUtils.nullToEmpty(le.getThreadName()))
                .loggerName(LogUtils.nullToEmpty(le.getLoggerName()))
                .message(LogUtils.nullToEmpty(le.getFormattedMessage()))
                .throwable(getThrowable(le))
                .mdc(getMdc(le));

        if (le.getMarker() != null) {
            builder.marker(le.getMarker().getName());
        }

        StackTraceElement[] callerData = le.getCallerData();

        if (callerData != null && callerData.length > 0) {
            builder.className(LogUtils.nullToEmpty(callerData[0].getClassName()))
                    .methodName(LogUtils.nullToEmpty(callerData[0].getMethodName()))
                    .lineNumber(Integer.toString(callerData[0].getLineNumber()))
                    .fileName(LogUtils.nullToEmpty(callerData[0].getFileName()));
        }
        return builder.build();
    }

    private String getThrowable(ILoggingEvent le) {
        IThrowableProxy throwableProxy = le.getThrowableProxy();
        StringBuilder throwable = new StringBuilder();

        while (throwableProxy != null) {
            throwable.append(throwableProxy.getClassName())
                    .append(": ")
                    .append(throwableProxy.getMessage())
                    .append(System.lineSeparator());

            StackTraceElementProxy[] stackTrace = throwableProxy.getStackTraceElementProxyArray();

            for (StackTraceElementProxy step : stackTrace) {
                throwable.append("\t").append(step);
                ClassPackagingData cpd = step.getClassPackagingData();

                if (cpd != null) {
                    if (cpd.isExact()) {
                        throwable.append(" [");
                    } else {
                        throwable.append(" ~[");
                    }
                    throwable.append(cpd.getCodeLocation()).append(':').append(cpd.getVersion()).append(']');
                }
                throwable.append(System.lineSeparator());
            }
            throwableProxy = throwableProxy.getCause();
        }
        return throwable.toString().trim();
    }

    private String getMdc(ILoggingEvent le) {
        Map<String, String> mdcMap = le.getMDCPropertyMap();
        String mdc = "";

        if (mdcMap != null && !mdcMap.isEmpty()) {
            mdc = mdcMap.toString();
        }
        return mdc;
    }
}
