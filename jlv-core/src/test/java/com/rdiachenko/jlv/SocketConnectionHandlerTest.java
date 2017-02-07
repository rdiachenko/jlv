package com.rdiachenko.jlv;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.eventbus.EventBus;
import com.rdiachenko.jlv.TestUtils.LogCollector;

public class SocketConnectionHandlerTest {
    
    private static final String LOG_MESSAGE_1 = "simple message 1";
    private static final String LOG_MESSAGE_2 = "simple message 2";
    
    private final EventBus eventBus = new EventBus();
    private final List<Log> buffer = new ArrayList<>();
    private final LogCollector logCollector = TestUtils.createLogCollector(buffer);
    
    @Before
    public void before() throws IOException {
        eventBus.register(logCollector);
    }
    
    @After
    public void after() {
        eventBus.unregister(logCollector);
        buffer.clear();
    }
    
    @Test
    public void testLog4j1Handling() throws IOException {
        org.apache.log4j.spi.LoggingEvent[] logs = {
                TestUtils.createSimpleLog4j1Log(LOG_MESSAGE_1),
                TestUtils.createSimpleLog4j1Log(LOG_MESSAGE_2),
        };
        Socket socket = mockSocket(TestUtils.objectsToBytes(logs));
        SocketConnectionHandler logHandler = new SocketConnectionHandler(socket, eventBus);
        logHandler.run();
        Assert.assertTrue(buffer.size() == 2);
        Assert.assertEquals(LOG_MESSAGE_1, buffer.get(0).getMessage());
        Assert.assertEquals(LOG_MESSAGE_2, buffer.get(1).getMessage());
    }
    
    @Test
    public void testLog4j2Handling() throws IOException {
        org.apache.logging.log4j.core.LogEvent[] logs = {
                TestUtils.createSimpleLog4j2Log(LOG_MESSAGE_1),
                TestUtils.createSimpleLog4j2Log(LOG_MESSAGE_2),
        };
        Socket socket = mockSocket(TestUtils.objectsToBytes(logs));
        SocketConnectionHandler logHandler = new SocketConnectionHandler(socket, eventBus);
        logHandler.run();
        Assert.assertTrue(buffer.size() == 2);
        Assert.assertEquals(LOG_MESSAGE_1, buffer.get(0).getMessage());
        Assert.assertEquals(LOG_MESSAGE_2, buffer.get(1).getMessage());
    }
    
    @Test
    public void testLogbackHandling() throws IOException {
        ch.qos.logback.classic.spi.ILoggingEvent[] logs = {
                TestUtils.createSimpleLogbackLog(LOG_MESSAGE_1),
                TestUtils.createSimpleLogbackLog(LOG_MESSAGE_2),
        };
        Socket socket = mockSocket(TestUtils.objectsToBytes(logs));
        SocketConnectionHandler logHandler = new SocketConnectionHandler(socket, eventBus);
        logHandler.run();
        Assert.assertTrue(buffer.size() == 2);
        Assert.assertEquals(LOG_MESSAGE_1, buffer.get(0).getMessage());
        Assert.assertEquals(LOG_MESSAGE_2, buffer.get(1).getMessage());
    }
    
    private static Socket mockSocket(byte[] input) throws IOException {
        InputStream in = new ByteArrayInputStream(input);
        Socket socket = mock(Socket.class);
        when(socket.getInputStream()).thenReturn(in);
        return socket;
    }
}
