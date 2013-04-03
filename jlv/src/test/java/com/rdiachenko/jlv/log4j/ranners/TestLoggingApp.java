package com.rdiachenko.jlv.log4j.ranners;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLoggingApp {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public static void main(String[] args) {
        PropertyConfigurator.configure("src/test/resources/log4j-test.properties");
        TestLoggingApp app = new TestLoggingApp();
        app.run();
    }

	private void run() {
		logger.debug("debug test message 1");
		logger.info("info test message 1");
		logger.error("error test message 1");
	}
}
