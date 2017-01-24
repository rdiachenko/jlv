package com.rdiachenko.jlv;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;
import org.junit.Assert;
import org.junit.Test;

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
  
  private static final String[] THROWABLE_INFO = {
      "Exception in thread \"main\" java.lang.IllegalStateException: test exception",
      "at A.main(A.java:6)"
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
    
    LoggingEvent mockLoggingEvent = mock(LoggingEvent.class);
    setFinalField(mockLoggingEvent, LoggingEvent.class.getDeclaredField("timeStamp"), NOW);
    when(mockLoggingEvent.getLevel()).thenReturn(Level.toLevel(LEVEL));
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
  
  private static void setFinalField(Object obj, Field field, Object newValue)
      throws NoSuchFieldException, IllegalAccessException {
    field.setAccessible(true);
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    field.set(obj, newValue);
  }
}
