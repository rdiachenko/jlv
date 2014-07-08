package com.github.rd.jlv.log4j.socketappender;

import java.io.IOException;

import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Test;

public class ServerTest {

//	private static LogDao logDao;

	private int port = 7777;

	private String host = "localhost";

//	@BeforeClass
//	public static void init() {
//		logDao = DaoProvider.LOG_DAO.getLogDao();
//	}
//
//	@Before
//	public void initDb() {
//		logDao.initDb();
//	}
//
//	@After
//	public void dropDb() {
//		logDao.dropDb();
//	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativePortNumber() throws IOException {
		new SocketLogServer(-7);
	}

	@Test(expected = IOException.class)
	public void testOnePortUsagePerMultipleServers() throws IOException {
		SocketLogServer server1 = null;
		SocketLogServer server2 = null;

		try {
			server1 = new SocketLogServer(port);
			server2 = new SocketLogServer(port);
		} finally {
			if (server1 != null) {
				server1.shutdown();
			}
			if (server2 != null) {
				server2.shutdown();
			}
		}
	}

//	@Test
//	public void testServer() throws IOException, InterruptedException {
//		LoggingEvent mockLoggingEvent1 = generateLoggingEventByMessage("message1");
//		LoggingEvent mockLoggingEvent2 = generateLoggingEventByMessage("message2");
//		Log[] expectedLogList = {
//				LogUtil.convert(mockLoggingEvent1),
//				LogUtil.convert(mockLoggingEvent2),
//		};
//
//		Server server = new Server(port);
//		server.start();
//
//		Socket client = new Socket(host, port);
//		ObjectOutputStream mockOutputStream = new ObjectOutputStream(client.getOutputStream());
//		mockOutputStream.writeObject(mockLoggingEvent1);
//		mockOutputStream.writeObject(mockLoggingEvent2);
//		mockOutputStream.flush();
//		client.close();
//		Thread.sleep(1000);
//		server.shutdown();
//
//		// Logs from db
//		LogContainer logContainer = logDao.getTailingLogs(2);
//
//		Assert.assertTrue(logContainer.size() == 2);
//
//		Log[] logsFromDb = new Log[2];
//		Iterator<Log> iter = logContainer.iterator();
//		logsFromDb[1] = iter.next();
//		logsFromDb[0] = iter.next();
//
//		Assert.assertArrayEquals(expectedLogList, logsFromDb);
//	}
//
//	@Test(expected = SocketException.class)
//	public void testForceServerStop() throws IOException, InterruptedException {
//		LoggingEvent mockLoggingEvent1 = generateLoggingEventByMessage("message1");
//		LoggingEvent mockLoggingEvent2 = generateLoggingEventByMessage("message2");
//
//		Server server = new Server(port);
//		server.start();
//
//		Socket client = new Socket(host, port);
//		ObjectOutputStream mockOutputStream = new ObjectOutputStream(client.getOutputStream());
//		mockOutputStream.writeObject(mockLoggingEvent1);
//		mockOutputStream.flush();
//
//		Thread.sleep(1000);
//		server.shutdown();
//
//		// Logs from db
//		LogContainer logContainer = logDao.getTailingLogs(3);
//		Assert.assertTrue(logContainer.size() == 1);
//		Assert.assertEquals(LogUtil.convert(mockLoggingEvent1), logContainer.get());
//
//		mockOutputStream.writeObject(mockLoggingEvent2);
//		client.close();
//	}
//
//	@Test
//	public void testMultipleClients() throws IOException, InterruptedException {
//		LoggingEvent mockLoggingEvent1 = generateLoggingEventByMessage("message1");
//		LoggingEvent mockLoggingEvent2 = generateLoggingEventByMessage("message2");
//		LoggingEvent mockLoggingEvent3 = generateLoggingEventByMessage("message3");
//		LoggingEvent mockLoggingEvent4 = generateLoggingEventByMessage("message4");
//
//		Server server = new Server(port);
//		server.start();
//
//		Socket client1 = new Socket(host, port);
//		ObjectOutputStream mockOutputStream1 = new ObjectOutputStream(client1.getOutputStream());
//		mockOutputStream1.writeObject(mockLoggingEvent1);
//		mockOutputStream1.flush();
//
//		Socket client2 = new Socket(host, port);
//		ObjectOutputStream mockOutputStream2 = new ObjectOutputStream(client2.getOutputStream());
//		mockOutputStream2.writeObject(mockLoggingEvent4);
//		mockOutputStream2.flush();
//		mockOutputStream2.writeObject(mockLoggingEvent2);
//		mockOutputStream2.flush();
//
//		mockOutputStream1.writeObject(mockLoggingEvent3);
//		mockOutputStream1.flush();
//
//		Thread.sleep(1000);
//		client2.close();
//		server.shutdown();
//		client1.close();
//
//		// Logs from db
//		LogContainer logContainer = logDao.getTailingLogs(8);
//
//		Assert.assertTrue(logContainer.size() == 4);
//		Assert.assertTrue(logContainer.contains(LogUtil.convert(mockLoggingEvent1)));
//		Assert.assertTrue(logContainer.contains(LogUtil.convert(mockLoggingEvent2)));
//		Assert.assertTrue(logContainer.contains(LogUtil.convert(mockLoggingEvent3)));
//		Assert.assertTrue(logContainer.contains(LogUtil.convert(mockLoggingEvent4)));
//	}

	private LoggingEvent generateLoggingEventByMessage(String message) {
		// Creating mock LoggingEvent
		class CategoryExt extends Category {
			protected CategoryExt(String name) {
				super(name);
			}
		}

		Category mockCategory = new CategoryExt(null);
		LoggingEvent mockLoggingEvent = new LoggingEvent(null, mockCategory, 1234567L, Level.INFO, message, null);
		return mockLoggingEvent;
	}
}
