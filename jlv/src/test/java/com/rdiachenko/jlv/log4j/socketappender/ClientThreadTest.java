package com.rdiachenko.jlv.log4j.socketappender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientThreadTest {

	private static File logFile;

	private ClientApp client;

	private ExecutorService executor;

	private ServerRunner serverRunner;

	@Test
	public void testClient() throws IOException {
		client.logger.debug("debug test message 1");
		client.logger.info("info test message 1");
		client.logger.error("error test message 1");

		Assert.assertTrue(logFile.exists());
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(logFile));

			String log = reader.readLine();
			Assert.assertNotNull(log);
			Assert.assertTrue(log.endsWith("[debug test message 1]"));

			log = reader.readLine();
			Assert.assertNotNull(log);
			Assert.assertTrue(log.endsWith("[info test message 1]"));

			log = reader.readLine();
			Assert.assertNotNull(log);
			Assert.assertTrue(log.endsWith("[error test message 1]"));
		} finally {
			reader.close();
		}

	}

	@BeforeClass
	public static void init() {
		logFile = new File("src/main/resources/log4j.log");
	}

	@AfterClass
	public static void clean() {
		if (logFile.exists()) {
			logFile.delete();
		}
	}

	@Before
	public void initServerAndClient() throws IOException {
		executor = Executors.newSingleThreadExecutor();
		serverRunner = new ServerRunner();
		executor.execute(serverRunner);
		client = new ClientApp();
	}

	@After
	public void stopServerAndClient() throws IOException, InterruptedException {
		serverRunner.stop();
		executor.shutdown();
		if (!executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)) {
			executor.shutdownNow();
		}
	}

	private static class ClientApp {

		private final Logger logger = LoggerFactory.getLogger(getClass());

		private ClientApp() {
			PropertyConfigurator.configure("src/test/resources/log4j-test.properties");
		}
	}

	private static class ServerRunner implements Runnable {

		private Server server;

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
