package com.rdiachenko.jlv.log4j.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rdiachenko.jlv.log4j.domain.Log;
import com.rdiachenko.jlv.log4j.domain.LogContainer;
import com.rdiachenko.jlv.log4j.domain.LogFieldName;

public class LogDaoImpl implements LogDao {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private String insertLogQueryString = "INSERT INTO log4j1x "
			+ "("
			+ LogFieldName.CATEGORY.getName()
			+ ", " + LogFieldName.CLASS.getName()
			+ ", " + LogFieldName.DATE.getName()
			+ ", " + LogFieldName.FILE.getName()
			+ ", " + LogFieldName.LOCATION_INFO.getName()
			+ ", " + LogFieldName.LINE.getName()
			+ ", " + LogFieldName.METHOD.getName()
			+ ", " + LogFieldName.LEVEL.getName()
			+ ", " + LogFieldName.MILLISECONDS.getName()
			+ ", " + LogFieldName.THREAD.getName()
			+ ", " + LogFieldName.MESSAGE.getName()
			+ ", " + LogFieldName.THROWABLE.getName()
			+ ") "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private String createTableQueryString = "CREATE TABLE log4j1x("
			+ "ID BIGINT AUTO_INCREMENT,"
			+ LogFieldName.CATEGORY.getName() + " VARCHAR(1000) DEFAULT '',"
			+ LogFieldName.CLASS.getName() + " VARCHAR(1000) DEFAULT '',"
			+ LogFieldName.DATE.getName() + " VARCHAR(100) DEFAULT '',"
			+ LogFieldName.FILE.getName() + " VARCHAR(1000) DEFAULT '',"
			+ LogFieldName.LOCATION_INFO.getName() + " VARCHAR(1000) DEFAULT '',"
			+ LogFieldName.LINE.getName() + " VARCHAR(100) DEFAULT '',"
			+ LogFieldName.METHOD.getName() + " VARCHAR(1000) DEFAULT '',"
			+ LogFieldName.LEVEL.getName() + " VARCHAR(100) DEFAULT '',"
			+ LogFieldName.MILLISECONDS.getName() + " VARCHAR(1000) DEFAULT '',"
			+ LogFieldName.THREAD.getName() + " VARCHAR(1000) DEFAULT '',"
			+ LogFieldName.MESSAGE.getName() + " VARCHAR(MAX) DEFAULT '',"
			+ LogFieldName.THROWABLE.getName() + " VARCHAR(MAX) DEFAULT ''"
			+ ")";

	private ConnectionPool connectionPool;

	public LogDaoImpl() {
		connectionPool = new ConnectionPool();
	}

	public void initDb() {
		dropLogsTable();
		createLogsTable();
	}

	public void dropDb() {
		dropLogsTable();
	}

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
				Log log = (new Log.Builder()).categoryName(result.getString(LogFieldName.CATEGORY.getName()))
						.className(result.getString(LogFieldName.CLASS.getName()))
						.date(result.getString(LogFieldName.DATE.getName()))
						.fileName(result.getString(LogFieldName.FILE.getName()))
						.locationInfo(result.getString(LogFieldName.LOCATION_INFO.getName()))
						.lineNumber(result.getString(LogFieldName.LINE.getName()))
						.methodName(result.getString(LogFieldName.METHOD.getName()))
						.level(result.getString(LogFieldName.LEVEL.getName()))
						.ms(result.getString(LogFieldName.MILLISECONDS.getName()))
						.threadName(result.getString(LogFieldName.THREAD.getName()))
						.message(result.getString(LogFieldName.MESSAGE.getName()))
						.throwable(result.getString(LogFieldName.THROWABLE.getName()))
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
