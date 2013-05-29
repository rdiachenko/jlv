package com.rdiachenko.jlv.log4j.socketappender;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rdiachenko.jlv.log4j.dao.LogDao;
import com.rdiachenko.jlv.log4j.dao.LogDaoImpl;
import com.rdiachenko.jlv.log4j.domain.Log;

public class ClientThreadTest {

	private LogDao logDao;

	private ServerRunner serverRunner;

	private ExecutorService executor;

	@Test
	public void testClient() throws InterruptedException {
		Logger logger = LoggerFactory.getLogger(getClass());
		PropertyConfigurator.configure("src/test/resources/log4j-test.properties");

		final String[] expectedLogMessages = {
				"debug test message 1",
				"info test message 1",
				"error test message 1",
		};

		logger.debug(expectedLogMessages[0]);
		logger.info(expectedLogMessages[1]);
		logger.error(expectedLogMessages[2]);

		// Reset configuration in order the only the above three logs are inserted to db
		PropertyConfigurator.configure("src/main/resources/log4j.properties");

		// Wait for logs
		Thread.sleep(2000);
		Iterator<Log> logs = logDao.getTailingLogs(3).iterator();

		Assert.assertTrue(logs.hasNext());
		Assert.assertEquals(expectedLogMessages[2], logs.next().getMessage());

		Assert.assertTrue(logs.hasNext());
		Assert.assertEquals(expectedLogMessages[1], logs.next().getMessage());

		Assert.assertTrue(logs.hasNext());
		Assert.assertEquals(expectedLogMessages[0], logs.next().getMessage());

		Assert.assertFalse(logs.hasNext());
	}

	@Before
	public void init() throws IOException {
		logDao = new LogDaoImpl();
		logDao.initDb();
		executor = Executors.newSingleThreadExecutor();
		serverRunner = new ServerRunner();
		executor.execute(serverRunner);
	}

	@After
	public void stop() throws InterruptedException {
		serverRunner.stop();
		executor.shutdown();
		if (!executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)) {
			executor.shutdownNow();
		}
		logDao.dropDb();
	}

	private final static class ServerRunner implements Runnable {

		private final Server server;

		private ServerRunner() throws IOException {
			server = new Server(4445);
		}

		public void run() {
			server.start();
		}

		public void stop() {
			server.stop();
		}
	}
}
