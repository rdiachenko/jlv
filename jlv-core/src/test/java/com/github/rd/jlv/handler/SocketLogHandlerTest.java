package com.github.rd.jlv.handler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import org.apache.log4j.spi.LoggingEvent;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.rd.jlv.CircularBuffer;
import com.github.rd.jlv.Log;
import com.github.rd.jlv.TestUtils;
import com.github.rd.jlv.TestUtils.LogCollector;
import com.google.common.eventbus.EventBus;

public class SocketLogHandlerTest {

	private static final int BUFFER_SIZE = 5;
	private static final String LOG_MESSAGE_1 = "log1";
	private static final String LOG_MESSAGE_2 = "log2";
	private static final String LOG_MESSAGE_3 = "log3";

	private static final LoggingEvent[] LOG4J_1_LOGS = {
			TestUtils.createLog4j1LogFromMessage(LOG_MESSAGE_1),
			TestUtils.createLog4j1LogFromMessage(LOG_MESSAGE_2),
			TestUtils.createLog4j1LogFromMessage(LOG_MESSAGE_3),
	};

	private final EventBus eventBus = new EventBus();
	private CircularBuffer<Log> buffer;
	private LogCollector logCollector;
	private Socket socket;

	@Before
	public void init() throws IOException {
		buffer = new CircularBuffer<>(BUFFER_SIZE);
		logCollector = TestUtils.createLogCollector(buffer);
		eventBus.register(logCollector);
		InputStream in = new ByteArrayInputStream(TestUtils.objectsToBytes(LOG4J_1_LOGS));
		socket = mock(Socket.class);
		when(socket.getInputStream()).thenReturn(in);
	}

	@After
	public void clean() {
		eventBus.unregister(logCollector);
	}

	@Test
	public void testLogHandling() throws IOException {
		SocketLogHandler logHandler = new SocketLogHandler(socket, eventBus);
		logHandler.run();

		Assert.assertTrue("wrong buffer size", buffer.size() == 3);
		Assert.assertEquals(LOG_MESSAGE_1, buffer.get(0).getMessage());
		Assert.assertEquals(LOG_MESSAGE_2, buffer.get(1).getMessage());
		Assert.assertEquals(LOG_MESSAGE_3, buffer.get(2).getMessage());
	}
}
