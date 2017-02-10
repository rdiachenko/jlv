package com.rdiachenko.jlv;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.rdiachenko.jlv.TestUtils.LogCollector;

public class SocketLogServerTest {

    private static final String HOST = "localhost";
    private static final int PORT = 7778;
    private static final int WAIT_MS = 10000;
    
    private static final String LOG4J_1_MESSAGE = "simple message from log4j1";
    private static final String LOG4J_2_MESSAGE = "simple message from log4j2";
    private static final String LOGBACK_MESSAGE = "simple message from logback";

    private final List<Log> buffer = Collections.synchronizedList(new ArrayList<>());
    private final LogCollector logCollector = TestUtils.createLogCollector(buffer);
    private SocketLogServer server = new SocketLogServer(PORT);

    @Before
    public void before() {
        server.addLogEventListener(logCollector);
        server.start();
    }

    @After
    public void after() {
        server.removeLogEventListener(logCollector);
        server.stop();
        buffer.clear();
    }
    
    @Test
    public void testOneClient() throws IOException, InterruptedException {
        try (Socket socket = new Socket(HOST, PORT);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            out.writeObject(TestUtils.createSimpleLog4j1Log(LOG4J_1_MESSAGE));
            out.writeObject(TestUtils.createSimpleLog4j2Log(LOG4J_2_MESSAGE));
            out.writeObject(TestUtils.createSimpleLogbackLog(LOGBACK_MESSAGE));
        }
        waitForBufferSize(buffer, 3);
        Assert.assertTrue(buffer.size() == 3);
        Assert.assertEquals(LOG4J_1_MESSAGE, buffer.get(0).getMessage());
        Assert.assertEquals(LOG4J_2_MESSAGE, buffer.get(1).getMessage());
        Assert.assertEquals(LOGBACK_MESSAGE, buffer.get(2).getMessage());
    }
    
    @Test
    public void testMultipleClients() throws IOException, InterruptedException {
        try (Socket socket1 = new Socket(HOST, PORT);
                ObjectOutputStream out1 = new ObjectOutputStream(socket1.getOutputStream());
                Socket socket2 = new Socket(HOST, PORT);
                ObjectOutputStream out2 = new ObjectOutputStream(socket2.getOutputStream());
                Socket socket3 = new Socket(HOST, PORT);
                ObjectOutputStream out3 = new ObjectOutputStream(socket3.getOutputStream())) {
            out1.writeObject(TestUtils.createSimpleLog4j1Log(LOG4J_1_MESSAGE));
            out2.writeObject(TestUtils.createSimpleLog4j2Log(LOG4J_2_MESSAGE));
            out2.writeObject(TestUtils.createSimpleLogbackLog(LOGBACK_MESSAGE));
            out3.writeObject(TestUtils.createSimpleLog4j1Log(LOG4J_1_MESSAGE));
            out1.writeObject(TestUtils.createSimpleLog4j2Log(LOG4J_2_MESSAGE));
            out1.writeObject(TestUtils.createSimpleLogbackLog(LOGBACK_MESSAGE));
            out3.writeObject(TestUtils.createSimpleLog4j2Log(LOG4J_2_MESSAGE));
        }
        waitForBufferSize(buffer, 7);
        Collections.sort(buffer, new Comparator<Log>() {
            @Override
            public int compare(Log log1, Log log2) {
                return log1.getMessage().compareTo(log2.getMessage());
            }
        });
        Assert.assertTrue(buffer.size() == 7);
        Assert.assertEquals(LOG4J_1_MESSAGE, buffer.get(0).getMessage());
        Assert.assertEquals(LOG4J_1_MESSAGE, buffer.get(1).getMessage());
        Assert.assertEquals(LOG4J_2_MESSAGE, buffer.get(2).getMessage());
        Assert.assertEquals(LOG4J_2_MESSAGE, buffer.get(3).getMessage());
        Assert.assertEquals(LOG4J_2_MESSAGE, buffer.get(4).getMessage());
        Assert.assertEquals(LOGBACK_MESSAGE, buffer.get(5).getMessage());
        Assert.assertEquals(LOGBACK_MESSAGE, buffer.get(6).getMessage());
    }

    private static void waitForBufferSize(Collection<?> buffer, int waitSize) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        long curTime = startTime;

        while (buffer.size() < waitSize && (curTime - startTime) <= WAIT_MS) {
            Thread.sleep(200);
            curTime = System.currentTimeMillis();
        }
    }
}
