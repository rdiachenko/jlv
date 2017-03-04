package com.rdiachenko.jlv;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;

import com.google.common.eventbus.Subscribe;

import ch.qos.logback.classic.spi.LoggingEventVO;

public final class TestUtils {

  private TestUtils() {
    // Utility class
  }

  public static <T> byte[] objectsToBytes(T[] objects) throws IOException {
    byte[] bytes = new byte[0];

    try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos)) {

      for (T obj : objects) {
        out.writeObject(obj);
      }
      bytes = bos.toByteArray();
    }
    return bytes;
  }

  public static ch.qos.logback.classic.spi.ILoggingEvent createSimpleLogbackLog(String message) {
    ch.qos.logback.classic.spi.LoggingEvent log = new ch.qos.logback.classic.spi.LoggingEvent();
    log.setLevel(ch.qos.logback.classic.Level.DEBUG);
    log.setMessage(message);
    return LoggingEventVO.build(log);
  }

  public static org.apache.log4j.spi.LoggingEvent createSimpleLog4j1Log(String message) {
    return new org.apache.log4j.spi.LoggingEvent(
        null, null, 0, org.apache.log4j.Level.DEBUG, message, null, null, null, null, null);
  }

  public static org.apache.logging.log4j.core.LogEvent createSimpleLog4j2Log(String message) {
    return org.apache.logging.log4j.core.impl.Log4jLogEvent.newBuilder()
        .setMessage(new org.apache.logging.log4j.message.FormattedMessage(message)).build();
  }

  public static LogCollector createLogCollector(Collection<Log> buffer) {
    return new LogCollector(buffer);
  }

  public static final class LogCollector {

    private final Collection<Log> buffer;

    private CountDownLatch latch;

    public LogCollector(Collection<Log> buffer) {
      this.buffer = buffer;
    }

    public void setLatch(CountDownLatch latch) {
      this.latch = latch;
    }

    @Subscribe
    public void handle(Log log) {
      buffer.add(log);

      if (latch != null) {
        latch.countDown();
      }
    }
  }
}
