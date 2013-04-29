package com.rdiachenko.jlv.log4j.ranners;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TestLoggingApp {

	public static void main(String[] args) throws InterruptedException {
		PropertyConfigurator.configure("src/test/resources/log4j-test.properties");
		TestLoggingApp app = new TestLoggingApp();
		app.run();
	}

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private TestLoggingApp() {
		// it is a runner class, no reason to have a public constructor
	}

	private void run() throws InterruptedException {
		for (int i = 0; i < 100; i++) {
			logger.debug("i=" + i + " - debug test message 1");
			logger.info("i=" + i + " - info test message 1");
			logger.error("i=" + i + " - error test message 1");
			Thread.sleep(500);
		}
	}
}
