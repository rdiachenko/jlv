package com.rdiachenko.jlv.log4j.socketappender;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rdiachenko.jlv.log4j.dao.LogDao;
import com.rdiachenko.jlv.log4j.dao.LogDaoImpl;
import com.rdiachenko.jlv.log4j.domain.Log;
import com.rdiachenko.jlv.log4j.domain.LogEventContainer;
import com.rdiachenko.jlv.log4j.domain.LogEventListener;

public class ClientThreadTest {

	private LogDao logDao;

	private ClientApp client;

	private ServerRunner serverRunner;

	private ExecutorService executor;

	@Test
	public void testClient() {
		String[] expectedLogMessages = {
				"debug test message 1",
				"info test message 1",
				"error test message 1",
		};
		final String[] actualLogMessages = new String[expectedLogMessages.length];

		LogEventListener listener = new LogEventListener() {
			private int index = 0;

			public void handleLogEvent(Log log) {
				actualLogMessages[index] = log.getMessage();
				index++;
			}
		};

		LogEventContainer.addListener(listener);

		client.logger.debug(expectedLogMessages[0]);
		client.logger.info(expectedLogMessages[1]);
		client.logger.error(expectedLogMessages[2]);

		LogEventContainer.removeListener(listener);

//		Assert.assertArrayEquals(expectedLogMessages, actualLogMessages);
	}

	@Before
	public void initServerAndClient() throws IOException {
		logDao = new LogDaoImpl();
		logDao.initDb();

		executor = Executors.newSingleThreadExecutor();
		serverRunner = new ServerRunner();
		executor.execute(serverRunner);
		client = new ClientApp();
	}

	@After
	public void stopServerAndClient() throws IOException, InterruptedException {
		logDao.dropDb();
		serverRunner.stop();
		executor.shutdown();

		if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
			executor.shutdownNow();
		}
	}

	private final static class ClientApp {

		private final Logger logger = LoggerFactory.getLogger(getClass());

		private ClientApp() {
			PropertyConfigurator.configure("src/test/resources/log4j-test.properties");
		}
	}

	private final static class ServerRunner implements Runnable {

		private final Server server;

		private ServerRunner() throws IOException {
			server = new Server(4445);
		}

		public void run() {
			try {
				server.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void stop() throws IOException {
			if (server != null) {
				server.stop();
			}
		}
	}
}
