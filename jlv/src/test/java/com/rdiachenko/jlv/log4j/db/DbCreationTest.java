package com.rdiachenko.jlv.log4j.db;

import java.util.Iterator;

import org.junit.Test;

import com.rdiachenko.jlv.log4j.dao.LogDao;
import com.rdiachenko.jlv.log4j.dao.LogDaoImpl;
import com.rdiachenko.jlv.log4j.domain.Log;

public class DbCreationTest {

	@Test
	public void testDb() {
		LogDao logDao = new LogDaoImpl();
//		logDao.dropAndCreateLogsTable();
		Iterator<Log> logs = logDao.getTailingLogs(10).iterator();

		while (logs.hasNext()) {
			System.out.println(logs.next());
		}
	}
}
