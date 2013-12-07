package com.github.rd.jlv.log4j.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rd.jlv.log4j.LogConstants;
import com.github.rd.jlv.log4j.domain.Log;
import com.github.rd.jlv.log4j.domain.LogContainer;

public class LogDaoImpl implements LogDao {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private String insertLogQueryString = "INSERT INTO log4j1x "
			+ "("
			+ LogConstants.CATEGORY
			+ ", " + LogConstants.CLASS
			+ ", " + LogConstants.DATE
			+ ", " + LogConstants.FILE
			+ ", " + LogConstants.LOCATION_INFO
			+ ", " + LogConstants.LINE
			+ ", " + LogConstants.METHOD
			+ ", " + LogConstants.LEVEL
			+ ", " + LogConstants.MILLISECONDS
			+ ", " + LogConstants.THREAD
			+ ", " + LogConstants.MESSAGE
			+ ", " + LogConstants.THROWABLE
			+ ") "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private String createTableQueryString = "CREATE TABLE log4j1x("
			+ "ID BIGINT AUTO_INCREMENT,"
			+ LogConstants.CATEGORY + " VARCHAR(1000) DEFAULT '',"
			+ LogConstants.CLASS + " VARCHAR(1000) DEFAULT '',"
			+ LogConstants.DATE + " VARCHAR(100) DEFAULT '',"
			+ LogConstants.FILE + " VARCHAR(1000) DEFAULT '',"
			+ LogConstants.LOCATION_INFO + " VARCHAR(1000) DEFAULT '',"
			+ LogConstants.LINE + " VARCHAR(100) DEFAULT '',"
			+ LogConstants.METHOD + " VARCHAR(1000) DEFAULT '',"
			+ LogConstants.LEVEL + " VARCHAR(100) DEFAULT '',"
			+ LogConstants.MILLISECONDS + " VARCHAR(1000) DEFAULT '',"
			+ LogConstants.THREAD + " VARCHAR(1000) DEFAULT '',"
			+ LogConstants.MESSAGE + " VARCHAR(MAX) DEFAULT '',"
			+ LogConstants.THROWABLE + " VARCHAR(MAX) DEFAULT ''"
			+ ")";

	private ConnectionPool connectionPool;

	public LogDaoImpl() {
		connectionPool = new ConnectionPool();
	}

	@Override
	public void initDb() {
		dropLogsTable();
		createLogsTable();
	}

	@Override
	public void dropDb() {
		dropLogsTable();
	}

	@Override
	public LogContainer getTailingLogs(int tail) {
		logger.info("Selection {} tailing logs from db...", tail);
		String queryString = "SELECT * FROM log4j1x ORDER BY id DESC LIMIT ?";
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		LogContainer logs = new LogContainer(tail);

		try {
			conn = connectionPool.getConnection();
			preparedStatement = conn.prepareStatement(queryString);
			preparedStatement.setInt(1, tail);
			result = preparedStatement.executeQuery();

			while (result.next()) {
				Log log = (new Log.Builder()).categoryName(result.getString(LogConstants.CATEGORY))
						.className(result.getString(LogConstants.CLASS))
						.date(result.getString(LogConstants.DATE))
						.fileName(result.getString(LogConstants.FILE))
						.locationInfo(result.getString(LogConstants.LOCATION_INFO))
						.lineNumber(result.getString(LogConstants.LINE))
						.methodName(result.getString(LogConstants.METHOD))
						.level(result.getString(LogConstants.LEVEL))
						.ms(result.getString(LogConstants.MILLISECONDS))
						.threadName(result.getString(LogConstants.THREAD))
						.message(result.getString(LogConstants.MESSAGE))
						.throwable(result.getString(LogConstants.THROWABLE))
						.build();
				logs.add(log);
			}
		} catch (SQLException e) {
			logger.error("", e);
		} finally {
			DaoUtil.close(conn, preparedStatement, result);
		}
		logger.info("{} logs were selected", logs.size());
		return logs;
	}

	@Override
	public void insert(Log log) {
		if (log == null) {
			throw new IllegalArgumentException("log is null");
		}

		Connection conn = null;
		PreparedStatement preparedStatement = null;

		try {
			conn = connectionPool.getConnection();
			preparedStatement = conn.prepareStatement(insertLogQueryString);
			preparedStatement.setString(1, log.getCategoryName());
			preparedStatement.setString(2, log.getClassName());
			preparedStatement.setString(3, log.getDate());
			preparedStatement.setString(4, log.getFileName());
			preparedStatement.setString(5, log.getLocationInfo());
			preparedStatement.setString(6, log.getLineNumber());
			preparedStatement.setString(7, log.getMethodName());
			preparedStatement.setString(8, log.getLevel());
			preparedStatement.setString(9, log.getMs());
			preparedStatement.setString(10, log.getThreadName());
			preparedStatement.setString(11, log.getMessage());
			preparedStatement.setString(12, log.getThrowable());
			preparedStatement.execute();
		} catch (SQLException e) {
			logger.error("", e);
			throw new IllegalStateException(e);
		} finally {
			DaoUtil.close(conn, preparedStatement);
		}
	}

	private void dropLogsTable() {
		String dropTableQueryString = "DROP TABLE log4j1x IF EXISTS";
		DaoUtil.executeQuery(connectionPool.getConnection(), dropTableQueryString);
		logger.info("log4j1x table was dropped");
	}

	private void createLogsTable() {
		DaoUtil.executeQuery(connectionPool.getConnection(), createTableQueryString);
		logger.info("log4j1x table was created");
	}
}
