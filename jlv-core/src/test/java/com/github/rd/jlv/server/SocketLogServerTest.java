package com.github.rd.jlv.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.rd.jlv.CircularBuffer;
import com.github.rd.jlv.Log;
import com.github.rd.jlv.TestUtils;
import com.github.rd.jlv.TestUtils.LogCollector;

public class SocketLogServerTest {

	private static final int BUFFER_SIZE = 5;
	private static final String HOST = "localhost";
	private static final int PORT = 7777;
	private static final int SEND_MESSAGE_DELAY = 200; // ms
	
	private static final String LOG_MESSAGE_1 = "log1";
	private static final String LOG_MESSAGE_2 = "log2";
	private static final String LOG_MESSAGE_3 = "log3";
	
	private CircularBuffer<Log> buffer;
	private LogCollector logCollector;
	private Server server;
	
	@Before
	public void init() {
		buffer = new CircularBuffer<>(BUFFER_SIZE);
		logCollector = TestUtils.createLogCollector(buffer);
		server = new SocketLogServer(PORT);
		server.addLogEventListener(logCollector);
		server.start();
	}
	
	@After
	public void clean() {
		server.removeLogEventListener(logCollector);
		server.stop();
	}
	
	@Test
	public void testServerOneClient() throws UnknownHostException, IOException, InterruptedException {
		try (Socket socket = new Socket(HOST, PORT);
                ObjectOutputStream out 
                    = new ObjectOutputStream(socket.getOutputStream())) {
            out.writeObject(TestUtils.createLog4j1LogFromMessage(LOG_MESSAGE_1));
            delay();
            out.writeObject(TestUtils.createLog4j1LogFromMessage(LOG_MESSAGE_2));
            delay();
        }
		Assert.assertTrue("wrong buffer size: " + buffer.size(), buffer.size() == 2);
		Assert.assertEquals(LOG_MESSAGE_1, buffer.get(0).getMessage());
		Assert.assertEquals(LOG_MESSAGE_2, buffer.get(1).getMessage());
	}
	
	@Test
	public void testServerTwoClients() throws UnknownHostException, IOException, InterruptedException {
		Socket client1 = new Socket(HOST, PORT);
		ObjectOutputStream out1 = new ObjectOutputStream(client1.getOutputStream());
		out1.writeObject(TestUtils.createLog4j1LogFromMessage(LOG_MESSAGE_1));
		delay();
		
		Socket client2 = new Socket(HOST, PORT);
		ObjectOutputStream out2 = new ObjectOutputStream(client2.getOutputStream());
		out2.writeObject(TestUtils.createLog4j1LogFromMessage(LOG_MESSAGE_3));
		out2.writeObject(TestUtils.createLog4j1LogFromMessage(LOG_MESSAGE_1));
		delay();

		out1.writeObject(TestUtils.createLog4j1LogFromMessage(LOG_MESSAGE_2));
		delay();
		
		client1.close();
		client2.close();
		
		Assert.assertTrue("wrong buffer size: " + buffer.size(), buffer.size() == 4);
		Assert.assertEquals(LOG_MESSAGE_1, buffer.get(0).getMessage());
		Assert.assertEquals(LOG_MESSAGE_3, buffer.get(1).getMessage());
		Assert.assertEquals(LOG_MESSAGE_1, buffer.get(2).getMessage());
		Assert.assertEquals(LOG_MESSAGE_2, buffer.get(3).getMessage());
	}
	
	private static void delay() throws InterruptedException {
		Thread.sleep(SEND_MESSAGE_DELAY);
	}
}
