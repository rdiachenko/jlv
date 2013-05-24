package com.rdiachenko.jlv.log4j.socketappender;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

//	@BeforeClass
//	public static void init() {
//
//	}





	@Test
	public void testClient() {

		LogEventListener listener = new LogEventListener() {
			public void handleLogEvent(Log log) {
				System.out.println("Inside listener: " + log);
			}
		};

		LogEventContainer.addListener(listener);

		client.logger.debug("debug test message 1");
		client.logger.info("info test message 1");
		client.logger.error("error test message 1");

		LogEventContainer.removeListener(listener);
	}



//	@AfterClass
//	public void clean() {
//		// drop db
//	}

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
	public void stopServerAndClient() throws IOException {
		serverRunner.stop();
		executor.shutdown();
		executor.shutdownNow();
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
