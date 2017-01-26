package com.rdiachenko.jlv;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.ThrowableInformation;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Marker;

import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;

public class LogConverterTest {

  private static final long NOW = System.currentTimeMillis();
  private static final String DATE = LogConverter.formatDate(NOW);
  private static final String LEVEL = "INFO";
  private static final String THREAD_NAME = "main";
  private static final String LOGGER_NAME = LogConverterTest.class.getName();
  private static final String MESSAGE = "simple message";
  private static final String CLASS_NAME = LogConverterTest.class.getName();
  private static final String METHOD_NAME = "test";
  private static final String LINE_NUMBER = "7";
  private static final String FILE_NAME = LogConverterTest.class.getSimpleName() + ".java";
  private static final String MARKER = "TEST";
  
  private static final String THROWABLE_MESSAGE = "test exception";
  private static final String[] THROWABLE_INFO = {
      "java.lang.IllegalStateException: " + THROWABLE_MESSAGE,
      "\tat com.rdiachenko.jlv.Main.main(Main.java:12)"
  };
  private static final String THROWABLE = THROWABLE_INFO[0] + System.lineSeparator() + THROWABLE_INFO[1];
  
  private static final String NDC = "ndc-value";
  
  private static final Map<String, String> MDC_MAP = new HashMap<>();
  static {
    MDC_MAP.put("mdc-key", "mdc-value");
  }
  private static final String MDC = MDC_MAP.toString();

  @Test
  public void testLog4j1EventConvertion() throws NoSuchFieldException, IllegalAccessException, SecurityException {
    LocationInfo mockLocationInfo = mock(LocationInfo.class);
    when(mockLocationInfo.getClassName()).thenReturn(CLASS_NAME);
    when(mockLocationInfo.getMethodName()).thenReturn(METHOD_NAME);
    when(mockLocationInfo.getLineNumber()).thenReturn(LINE_NUMBER);
    when(mockLocationInfo.getFileName()).thenReturn(FILE_NAME);
    
    ThrowableInformation mockThrowableInfo = mock(ThrowableInformation.class);
    when(mockThrowableInfo.getThrowableStrRep()).thenReturn(THROWABLE_INFO);
    
    org.apache.log4j.spi.LoggingEvent mockLoggingEvent = mock(org.apache.log4j.spi.LoggingEvent.class);
    setFinalField(mockLoggingEvent, org.apache.log4j.spi.LoggingEvent.class.getDeclaredField("timeStamp"), NOW);
    when(mockLoggingEvent.getLevel()).thenReturn(org.apache.log4j.Level.toLevel(LEVEL));
    when(mockLoggingEvent.getThreadName()).thenReturn(THREAD_NAME);
    when(mockLoggingEvent.getLoggerName()).thenReturn(LOGGER_NAME);
    when(mockLoggingEvent.getRenderedMessage()).thenReturn(MESSAGE);
    when(mockLoggingEvent.getLocationInformation()).thenReturn(mockLocationInfo);
    when(mockLoggingEvent.getThrowableInformation()).thenReturn(mockThrowableInfo);
    when(mockLoggingEvent.getNDC()).thenReturn(NDC);
    when(mockLoggingEvent.getProperties()).thenReturn(MDC_MAP);
    
    Log log = LogConverter.convert(mockLoggingEvent);
    Assert.assertEquals(DATE, log.getDate());
    Assert.assertEquals(LEVEL, log.getLevel());
    Assert.assertEquals(THREAD_NAME, log.getThreadName());
    Assert.assertEquals(LOGGER_NAME, log.getLoggerName());
    Assert.assertEquals(MESSAGE, log.getMessage());
    Assert.assertEquals(CLASS_NAME, log.getClassName());
    Assert.assertEquals(METHOD_NAME, log.getMethodName());
    Assert.assertEquals(LINE_NUMBER, log.getLineNumber());
    Assert.assertEquals(FILE_NAME, log.getFileName());
    Assert.assertTrue(log.getMarker().isEmpty());
    Assert.assertEquals(THROWABLE, log.getThrowable());
    Assert.assertEquals(NDC, log.getNdc());
    Assert.assertEquals(MDC, log.getMdc());
  }
  
  @Test
  public void testLogbackEventConvertion() {
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

    ch.qos.logback.classic.spi.LoggingEvent mockLoggingEvent = mock(ch.qos.logback.classic.spi.LoggingEvent.class);
    when(mockLoggingEvent.getTimeStamp()).thenReturn(NOW);
    when(mockLoggingEvent.getLevel()).thenReturn(ch.qos.logback.classic.Level.toLevel(LEVEL));
    when(mockLoggingEvent.getThreadName()).thenReturn(THREAD_NAME);
    when(mockLoggingEvent.getLoggerName()).thenReturn(LOGGER_NAME);
    when(mockLoggingEvent.getMessage()).thenReturn(MESSAGE);
    when(mockLoggingEvent.getFormattedMessage()).thenReturn(MESSAGE);
    when(mockLoggingEvent.getMarker()).thenReturn(mockMarker);
    when(mockLoggingEvent.getCallerData()).thenReturn(callerData);
    when(mockLoggingEvent.hasCallerData()).thenReturn(true);
    when(mockLoggingEvent.getThrowableProxy()).thenReturn(mockThrowableProxy);
    when(mockLoggingEvent.getMDCPropertyMap()).thenReturn(MDC_MAP);
    
    Log log = LogConverter.convert(ch.qos.logback.classic.spi.LoggingEventVO.build(mockLoggingEvent));
    Assert.assertEquals(DATE, log.getDate());
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
    Assert.assertEquals(MDC, log.getMdc());
  }
  
  private static void setFinalField(Object obj, Field field, Object newValue)
      throws NoSuchFieldException, IllegalAccessException {
    field.setAccessible(true);
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    field.set(obj, newValue);
  }
}
