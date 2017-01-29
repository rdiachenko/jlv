package com.rdiachenko.jlv.converter;

import java.util.HashMap;
import java.util.Map;

public final class ConverterFactory {
  
  private static final Map<Class<?>, Converter<?>> CONVERTERS = new HashMap<>();
  
  static {
    CONVERTERS.put(org.apache.log4j.spi.LoggingEvent.class, new Log4j1Converter());
    CONVERTERS.put(ch.qos.logback.classic.spi.ILoggingEvent.class, new LogbackConverter());
    CONVERTERS.put(org.apache.logging.log4j.core.LogEvent.class, new Log4j2Converter());
  }
  
  private ConverterFactory() {
    // Factory class
  }
  
  @SuppressWarnings("unchecked")
  public static <T> Converter<T> getConverter(Class<T> type) {
    if (CONVERTERS.containsKey(type)) {
      // we are sure that it is a safe casting
      return (Converter<T>) CONVERTERS.get(type);
    } else {
      throw new IllegalArgumentException("No converter for such a type: " + type);
    }
  }
}
