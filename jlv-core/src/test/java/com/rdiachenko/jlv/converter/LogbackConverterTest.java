package com.rdiachenko.jlv.converter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Marker;

import com.rdiachenko.jlv.Log;
import com.rdiachenko.jlv.LogUtils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.classic.spi.LoggingEventVO;
import ch.qos.logback.classic.spi.StackTraceElementProxy;

public class LogbackConverterTest {

  private static final long NOW = System.currentTimeMillis();
  private static final String LEVEL = "INFO";
  private static final String THREAD_NAME = "main";
  private static final String LOGGER_NAME = LogbackConverterTest.class.getName();
  private static final String MESSAGE = "simple message";
  private static final String CLASS_NAME = LogbackConverterTest.class.getName();
  private static final String METHOD_NAME = "test";
  private static final String LINE_NUMBER = "7";
  private static final String FILE_NAME = LogbackConverterTest.class.getSimpleName() + ".java";
  private static final String MARKER = "TEST";

  private static final String THROWABLE_MESSAGE = "test exception";
  private static final String THROWABLE = "java.lang.IllegalStateException: " + THROWABLE_MESSAGE
      + System.lineSeparator() + "\tat com.rdiachenko.jlv.Main.main(Main.java:12)";

  private static final Map<String, String> MDC_MAP = new HashMap<>();
  static {
    MDC_MAP.put("mdc-key", "mdc-value");
  }

  @Test
  public void testConvert() {
    Marker mockMarker = mock(Marker.class);
    when(mockMarker.getName()).thenReturn(MARKER);

    StackTraceElement[] callerData = {
        new StackTraceElement(CLASS_NAME, METHOD_NAME, FILE_NAME, Integer.parseInt(LINE_NUMBER))
    };

    StackTraceElementProxy stackTraceElementProxy = new StackTraceElementProxy(
        new StackTraceElement("com.rdiachenko.jlv.Main", "main", "Main.java", 12));
    IThrowableProxy mockThrowableProxy = mock(IThrowableProxy.class);
    when(mockThrowableProxy.getClassName()).thenReturn(IllegalStateException.class.getName());
    when(mockThrowableProxy.getMessage()).thenReturn(THROWABLE_MESSAGE);
    when(mockThrowableProxy.getStackTraceElementProxyArray())
        .thenReturn(new StackTraceElementProxy[] { stackTraceElementProxy });

    LoggingEvent mockLoggingEvent = mock(LoggingEvent.class);
    when(mockLoggingEvent.getTimeStamp()).thenReturn(NOW);
    when(mockLoggingEvent.getLevel()).thenReturn(Level.toLevel(LEVEL));
    when(mockLoggingEvent.getThreadName()).thenReturn(THREAD_NAME);
    when(mockLoggingEvent.getLoggerName()).thenReturn(LOGGER_NAME);
    when(mockLoggingEvent.getMessage()).thenReturn(MESSAGE);
    when(mockLoggingEvent.getFormattedMessage()).thenReturn(MESSAGE);
    when(mockLoggingEvent.getMarker()).thenReturn(mockMarker);
    when(mockLoggingEvent.getCallerData()).thenReturn(callerData);
    when(mockLoggingEvent.hasCallerData()).thenReturn(true);
    when(mockLoggingEvent.getThrowableProxy()).thenReturn(mockThrowableProxy);
    when(mockLoggingEvent.getMDCPropertyMap()).thenReturn(MDC_MAP);

    Log log = LogConverterType.LOGBACK.convert(LoggingEventVO.build(mockLoggingEvent));
    Assert.assertEquals(LogUtils.formatDate(NOW), log.getDate());
    Assert.assertEquals(LEVEL, log.getLevel());
    Assert.assertEquals(THREAD_NAME, log.getThreadName());
    Assert.assertEquals(LOGGER_NAME, log.getLoggerName());
    Assert.assertEquals(MESSAGE, log.getMessage());
    Assert.assertEquals(CLASS_NAME, log.getClassName());
    Assert.assertEquals(METHOD_NAME, log.getMethodName());
    Assert.assertEquals(LINE_NUMBER, log.getLineNumber());
    Assert.assertEquals(FILE_NAME, log.getFileName());
    Assert.assertEquals(MARKER, log.getMarker());
    Assert.assertEquals(THROWABLE, log.getThrowable());
    Assert.assertTrue(log.getNdc().isEmpty());
    Assert.assertEquals(MDC_MAP.toString(), log.getMdc());
  }
}
