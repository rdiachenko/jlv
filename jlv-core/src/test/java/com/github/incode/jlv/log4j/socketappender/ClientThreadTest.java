package com.github.incode.jlv.log4j.socketappender;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.incode.jlv.log4j.LogConverter;
import com.github.incode.jlv.log4j.dao.DaoProvider;
import com.github.incode.jlv.log4j.dao.LogDao;
import com.github.incode.jlv.log4j.domain.Log;
import com.github.incode.jlv.log4j.domain.LogContainer;
import com.github.incode.jlv.log4j.domain.LogEventContainer;
import com.github.incode.jlv.log4j.domain.LogEventListener;

public class ClientThreadTest {

	private static LogDao logDao;

	@BeforeClass
	public static void init() {
		logDao = DaoProvider.LOG_DAO.getLogDao();
	}

	@Before
	public void initDb() {
		logDao.initDb();
	}

	@After
	public void dropDb() {
		logDao.dropDb();
	}

	@Test
	public void testInplaceLogsReceiving() throws IOException, ClassNotFoundException {
		// Creating mock LoggingEvent
		class CategoryExt extends Category {
			protected CategoryExt(String name) {
				super(name);
			}
		}

		Category mockCategory = new CategoryExt(null);
		LoggingEvent mockLoggingEvent1 = new LoggingEvent(null, mockCategory, 1234567L, Level.INFO, "message1", null);
		LoggingEvent mockLoggingEvent2 = new LoggingEvent(null, mockCategory, 1234567L, Level.ERROR, "message2", null);
		LoggingEvent mockLoggingEvent3 = new LoggingEvent(null, mockCategory, 1234567L, Level.FATAL, "message3", null);

		// Reading LoggingEvent object from socket's input stream
		Socket client = writeLogsToSocket(Arrays.asList(mockLoggingEvent1, mockLoggingEvent2, mockLoggingEvent3));
		ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream());

		// Wrapping LoggingEvents into Log object in order to compare them
		Log expectedLog = LogConverter.convert(mockLoggingEvent1);
		Object logObj = inputStream.readObject();
		Log actualLog = LogConverter.convert((LoggingEvent) logObj);
		Assert.assertEquals(expectedLog, actualLog);

		expectedLog = LogConverter.convert(mockLoggingEvent2);
		logObj = inputStream.readObject();
		actualLog = LogConverter.convert((LoggingEvent) logObj);
		Assert.assertEquals(expectedLog, actualLog);

		expectedLog = LogConverter.convert(mockLoggingEvent3);
		logObj = inputStream.readObject();
		actualLog = LogConverter.convert((LoggingEvent) logObj);
		Assert.assertEquals(expectedLog, actualLog);
	}

	@Test
	public void testClientThread() throws UnknownHostException, IOException, InterruptedException,
			ClassNotFoundException {
		// Creating mock LoggingEvent
		class CategoryExt extends Category {
			protected CategoryExt(String name) {
				super(name);
			}
		}

		Category mockCategory = new CategoryExt(null);
		LoggingEvent mockLoggingEvent1 = new LoggingEvent(null, mockCategory, 1234567L, Level.INFO, "message1", null);
		LoggingEvent mockLoggingEvent2 = new LoggingEvent(null, mockCategory, 1234567L, Level.DEBUG, "message2", null);
		LoggingEvent mockLoggingEvent3 = new LoggingEvent(null, mockCategory, 1234567L, Level.ERROR, "message3", null);
		LoggingEvent mockLoggingEvent4 = new LoggingEvent(null, mockCategory, 1234567L, Level.FATAL, "message4", null);

		Log[] expectedLogList = {
				LogConverter.convert(mockLoggingEvent1),
				LogConverter.convert(mockLoggingEvent2),
				LogConverter.convert(mockLoggingEvent3),
				LogConverter.convert(mockLoggingEvent4),
		};

		// Logs from LogEventListener
		final Log[] actualLogList = new Log[4];
		LogEventListener listener = new LogEventListener() {
			private int counter = 0;

			public void lastLogEvent() {
				// no code
			}

			public void handleLogEvent(Log log) {
				actualLogList[counter] = log;
				counter++;
			}
		};
		LogEventContainer.addListener(listener);

		Socket client = writeLogsToSocket(Arrays.asList(mockLoggingEvent1, mockLoggingEvent2, mockLoggingEvent3,
				mockLoggingEvent4));
		Thread clientThread = new Thread(new ClientThread(client));
		clientThread.start();
		clientThread.join();
		LogEventContainer.removeListener(listener);

		// Logs from db
		LogContainer logContainer = logDao.getTailingLogs(4);

		Assert.assertTrue(logContainer.size() == 4);

		Log[] logsFromDb = new Log[4];
		Iterator<Log> iter = logContainer.iterator();
		logsFromDb[3] = iter.next();
		logsFromDb[2] = iter.next();
		logsFromDb[1] = iter.next();
		logsFromDb[0] = iter.next();

		Assert.assertArrayEquals(expectedLogList, actualLogList);
		Assert.assertArrayEquals(expectedLogList, logsFromDb);
	}

	@Test
	public void testNoneLoggingEventLogs() throws IOException, InterruptedException {
		Socket client = writeLogsToSocket(Arrays.asList("message1", "message2", "message3", "message4"));
		Thread clientThread = new Thread(new ClientThread(client));
		clientThread.start();
		clientThread.join();

		LogContainer logContainer = logDao.getTailingLogs(4);

		Assert.assertTrue(logContainer.size() == 0);
	}

	private Socket writeLogsToSocket(List<? extends Object> logs) throws IOException {
		// Defining the OutputStream for socket
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		Socket mockSocket = mock(Socket.class);
		when(mockSocket.getOutputStream()).thenReturn(byteArrayOutputStream);

		// Writing LoggingEvent object to socket's output stream
		ObjectOutputStream mockOutputStream = new ObjectOutputStream(mockSocket.getOutputStream());

		for (Object le : logs) {
			mockOutputStream.writeObject(le);
		}
		mockOutputStream.flush();

		// Now byteArrayOutputStream contains LoggingEvent object which we wrote on the previous step
		// Defining InputStream for socket
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
		when(mockSocket.getInputStream()).thenReturn(byteArrayInputStream);

		return mockSocket;
	}
}
