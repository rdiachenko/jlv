package com.github.rd.jlv;

import static org.mockito.Mockito.mock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

import com.google.common.eventbus.Subscribe;

public final class TestUtils {

	private TestUtils() {
		throw new IllegalStateException("Utils class constructor mustn't be called.");
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

	public static Log createLogFromMessage(String message) {
		return new Log.Builder().message(message).build();
	}

	public static LoggingEvent createLog4j1LogFromMessage(String message) {
		Category category = mock(Category.class);
		LoggingEvent log = new LoggingEvent(
				"categoryClass",
				category,
				System.currentTimeMillis(),
				Level.DEBUG,
				message,
				Thread.currentThread().getName(),
				null, null, null, null);
		return log;
	}

	public static LogCollector createLogCollector(CircularBuffer<Log> buffer) {
		return new LogCollector(buffer);
	}

	public static final class LogCollector {

		private CircularBuffer<Log> buffer;

		public LogCollector(CircularBuffer<Log> buffer) {
			this.buffer = buffer;
		}

		@Subscribe
		public void handle(Log log) {
			buffer.add(log);
		}
	}
}
