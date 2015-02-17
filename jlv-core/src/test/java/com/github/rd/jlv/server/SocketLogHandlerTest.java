package com.github.rd.jlv.server;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Assert;
import org.junit.Test;

import com.github.rd.jlv.CircularBuffer;
import com.github.rd.jlv.Log;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class SocketLogHandlerTest {

	@Test
	public void testLogHandling() throws IOException {
		CircularBuffer<Log> buffer = new CircularBuffer<>(5);
		LogCollector collector = new LogCollector(buffer);
		EventBus eventBus = new EventBus();
		eventBus.register(collector);
		
		Socket socket = mock(Socket.class);
		LoggingEvent[] logs = {
				createLogFromMessage("ping1"),
				createLogFromMessage("ping2"),
				createLogFromMessage("ping3"),
		};
		InputStream in = new ByteArrayInputStream(objectToBytes(logs));
		when(socket.getInputStream()).thenReturn(in);
		
		SocketLogHandler logHandler = new SocketLogHandler(socket, eventBus);
		logHandler.run();
		Assert.assertTrue("wrong buffer size", buffer.size() == 3);
		
		eventBus.unregister(collector);
	}
	
	private static <T> byte[] objectToBytes(T[] objects) throws IOException {
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
	
	private static LoggingEvent createLogFromMessage(String message) {
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
	
	private static final class LogCollector {
		
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
