package com.rdiachenko.jlv.log4j.socketappender;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ClientRunner {

	public static void main(String[] args) throws InterruptedException {
		Logger logger = LoggerFactory.getLogger(ClientRunner.class);
		PropertyConfigurator.configure("src/test/resources/log4j-test.properties");

		for (int i = 0; i < 5; i++) {
			logger.debug("i={}", i);
			logger.info("i={}", i);
			logger.error("i={}", i);
//			Thread.sleep(1000);
			try {
				int[] array = new int[2];
				int a = array[3];
			} catch (ArrayIndexOutOfBoundsException e) {
				logger.error("array exception:", e);
			}
		}
	}

	private ClientRunner() {
	}
}
