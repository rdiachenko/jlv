package com.github.rd.jlv;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.log4j.NDC;
import org.apache.log4j.PropertyConfigurator;

// TODO: move to a separate project (e.g. integration-tests, or test-runners, or...)
public final class Log4j1ClientApp {

	public static void main(String[] args) throws InterruptedException {
		Logger logger = Logger.getLogger(Log4j1ClientApp.class);

		PropertyConfigurator.configure("src/test/resources/log4j-test.properties");

		String exception = "Socket couldn't be started:\n"
				+ "java.net.SocketException: Socket closed\n"
				+ " at java.net.PlainSocketImpl.socketAccept(Native Method)\n"
				+ " at java.net.AbstractPlainSocketImpl.accept(AbstractPlainSocketImpl.java:398)\n"
				+ "	at java.net.ServerSocket.implAccept(ServerSocket.java:530)\n"
				+ "	at java.net.ServerSocket.accept(ServerSocket.java:498)\n"
				+ "	at com.rdiachenko.jlv.log4j.socketappender.Server.start(Server.java:38)\n"
				+ "	at com.rdiachenko.jlv.ui.views.JlvViewController$2.run(JlvViewController.java:79)\n"
				+ "	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145)\n"
				+ "	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)\n"
				+ "	at java.lang.Thread.run(Thread.java:724)";

		try {
			throw new IllegalStateException(exception);
		} catch (IllegalStateException e) {
			logger.fatal("illegal state:", e);
		}

		MDC.put("testKey1", "mdcTest1");
		MDC.put("testKey2", "mdcTest2");
		MDC.put("testKey3", "mdcTest3");

		for (int i = 0; i <= 1000; i++) {
			NDC.push("ndcTest" + i);
			logger.debug("i=" + i);
			logger.info("i=" + i);
			logger.error("i=" + i);
			NDC.pop();
			try {
				logger.info("retrieving element from array");
				int[] array = new int[2];
				int elem = array[3];
			} catch (ArrayIndexOutOfBoundsException e) {
				logger.error("exception message", e);
			}
			if (i % 3 == 0) {
				try {
					throw new IllegalStateException(exception);
				} catch (IllegalStateException e) {
					logger.error("illegal state:", e);
				}
			}
			Thread.sleep(1);
		}
		MDC.clear();
		NDC.clear();
	}

	private Log4j1ClientApp() {
	}
}
