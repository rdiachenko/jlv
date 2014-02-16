package com.github.rd.jlv.log4j.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rd.jlv.log4j.domain.Log;
import com.github.rd.jlv.log4j.domain.LogCollection;

public class LogDaoImpl implements LogDao {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private String insertLogQueryString = "INSERT INTO log4j1x "
			+ "("
			+ DbConstants.CATEGORY_FIELD_NAME
			+ ", " + DbConstants.CLASS_FIELD_NAME
			+ ", " + DbConstants.DATE_FIELD_NAME
			+ ", " + DbConstants.FILE_FIELD_NAME
			+ ", " + DbConstants.LOCATION_INFO_FIELD_NAME
			+ ", " + DbConstants.LINE_FIELD_NAME
			+ ", " + DbConstants.METHOD_FIELD_NAME
			+ ", " + DbConstants.LEVEL_FIELD_NAME
			+ ", " + DbConstants.MILLISECONDS_FIELD_NAME
			+ ", " + DbConstants.THREAD_FIELD_NAME
			+ ", " + DbConstants.MESSAGE_FIELD_NAME
			+ ", " + DbConstants.THROWABLE_FIELD_NAME
			+ ", " + DbConstants.NDC_FIELD_NAME
			+ ", " + DbConstants.MDC_FIELD_NAME
			+ ") "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private String createTableQueryString = "CREATE TABLE log4j1x("
			+ "ID BIGINT AUTO_INCREMENT,"
			+ DbConstants.CATEGORY_FIELD_NAME + " VARCHAR(1000) DEFAULT '',"
			+ DbConstants.CLASS_FIELD_NAME + " VARCHAR(1000) DEFAULT '',"
			+ DbConstants.DATE_FIELD_NAME + " VARCHAR(100) DEFAULT '',"
			+ DbConstants.FILE_FIELD_NAME + " VARCHAR(1000) DEFAULT '',"
			+ DbConstants.LOCATION_INFO_FIELD_NAME + " VARCHAR(1000) DEFAULT '',"
			+ DbConstants.LINE_FIELD_NAME + " VARCHAR(100) DEFAULT '',"
			+ DbConstants.METHOD_FIELD_NAME + " VARCHAR(1000) DEFAULT '',"
			+ DbConstants.LEVEL_FIELD_NAME + " VARCHAR(100) DEFAULT '',"
			+ DbConstants.MILLISECONDS_FIELD_NAME + " VARCHAR(1000) DEFAULT '',"
			+ DbConstants.THREAD_FIELD_NAME + " VARCHAR(1000) DEFAULT '',"
			+ DbConstants.MESSAGE_FIELD_NAME + " VARCHAR(MAX) DEFAULT '',"
			+ DbConstants.THROWABLE_FIELD_NAME + " VARCHAR(MAX) DEFAULT '',"
			+ DbConstants.NDC_FIELD_NAME + " VARCHAR(100) DEFAULT '',"
			+ DbConstants.MDC_FIELD_NAME + " VARCHAR(200) DEFAULT ''"
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
	public Log[] getTailingLogs(int tail) {
		logger.info("Selection {} tailing logs from db...", tail);
		String queryString = "SELECT * FROM log4j1x ORDER BY id DESC LIMIT ?";
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		LogCollection logs = new LogCollection(tail);

		try {
			conn = connectionPool.getConnection();
			preparedStatement = conn.prepareStatement(queryString);
			preparedStatement.setInt(1, tail);
			result = preparedStatement.executeQuery();

			while (result.next()) {
				Log log = (new Log.Builder()).categoryName(result.getString(DbConstants.CATEGORY_FIELD_NAME))
						.className(result.getString(DbConstants.CLASS_FIELD_NAME))
						.date(result.getString(DbConstants.DATE_FIELD_NAME))
						.fileName(result.getString(DbConstants.FILE_FIELD_NAME))
						.locationInfo(result.getString(DbConstants.LOCATION_INFO_FIELD_NAME))
						.lineNumber(result.getString(DbConstants.LINE_FIELD_NAME))
						.methodName(result.getString(DbConstants.METHOD_FIELD_NAME))
						.level(result.getString(DbConstants.LEVEL_FIELD_NAME))
						.ms(result.getString(DbConstants.MILLISECONDS_FIELD_NAME))
						.threadName(result.getString(DbConstants.THREAD_FIELD_NAME))
						.message(result.getString(DbConstants.MESSAGE_FIELD_NAME))
						.throwable(result.getString(DbConstants.THROWABLE_FIELD_NAME))
						.ndc(result.getString(DbConstants.NDC_FIELD_NAME))
						.mdc(result.getString(DbConstants.MDC_FIELD_NAME))
						.build();
				logs.add(log);
			}
		} catch (SQLException e) {
			logger.error("", e);
		} finally {
			DaoUtil.close(conn, preparedStatement, result);
		}
		logger.info("{} logs were selected", logs.size());
		return logs.toArray();
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
			preparedStatement.setString(13, log.getNdc());
			preparedStatement.setString(14, log.getMdc());
			preparedStatement.execute();
		} catch (SQLException e) {
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
