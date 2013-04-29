package com.rdiachenko.jlv.log4j.dao;

import java.util.Iterator;

import org.junit.Test;

import com.rdiachenko.jlv.log4j.domain.Log;
import com.rdiachenko.jlv.log4j.domain.LogEventContainer;
import com.rdiachenko.jlv.log4j.domain.LogEventListener;

public class DbCreationTest {

	@Test
	public void testDb() {
		LogDao logDao = new LogDaoImpl();
		logDao.initDb();

		Log log = (new Log.Builder()).categoryName("category")
				.className("class")
				.date("date")
				.fileName("file")
				.locationInfo("locInfo")
				.lineNumber("line")
				.methodName("method")
				.level("level")
				.ms("ms")
				.threadName("thread")
				.message("message")
				.build();

		LogEventListener listener = new LogEventListener() {
			public void handleLogEvent(Log log) {
				System.out.println("Inside listener: " + log);
			}
		};

		LogEventContainer.addListener(listener);
		logDao.insert(log);
		LogEventContainer.removeListener(listener);

		System.out.println("Iterator============");
		Iterator<Log> logs = logDao.getTailingLogs(10).iterator();

		while (logs.hasNext()) {
			System.out.println(logs.next());
		}

	}
}
