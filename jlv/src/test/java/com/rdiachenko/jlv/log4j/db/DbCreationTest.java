package com.rdiachenko.jlv.log4j.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;

import org.junit.Test;

import com.rdiachenko.jlv.log4j.dao.ConnectionFactory;
import com.rdiachenko.jlv.log4j.dao.LogDao;
import com.rdiachenko.jlv.log4j.dao.LogDaoImpl;
import com.rdiachenko.jlv.log4j.domain.Log;

public class DbCreationTest {

	@Test
	public void testDb() throws SQLException {
		Connection conn = ConnectionFactory.CONNECTION.getConnection();
		LogDao logDao = new LogDaoImpl();
		Iterator<Log> logs = logDao.getTailingLogs(1).iterator();

		while (logs.hasNext()) {
			System.out.println(logs.next());
		}
		conn.close();
	}
}
