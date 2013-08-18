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

	public void initDb() {
		dropLogsTable();
		createLogsTable();
		createLogsInsTrigger();
	}

	public void dropDb() {
		dropLogsTable();
	}

	public LogContainer getTailingLogs(int tail) {
		logger.info("Selection {} tailing logs from db...", tail);
		String queryString = "SELECT * FROM logs ORDER BY id DESC LIMIT ?";
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		LogContainer logs = new LogContainer(tail);

		try {
			conn = ConnectionFactory.CONNECTION.getConnection();
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
		String queryString = "INSERT INTO logs "
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
		Connection conn = null;
		PreparedStatement preparedStatement = null;

		try {
			conn = ConnectionFactory.CONNECTION.getConnection();
			preparedStatement = conn.prepareStatement(queryString);
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
		} finally {
			DaoUtil.close(conn, preparedStatement);
		}
	}

	private void dropLogsTable() {
		String dropTableQueryString = "DROP TABLE logs IF EXISTS";
		DaoUtil.executeQuery(dropTableQueryString);
		logger.info("Logs table was dropped");
	}

	private void createLogsTable() {
		String createTableQueryString = "CREATE TABLE logs("
				+ "ID BIGINT AUTO_INCREMENT,"
				+ LogFieldName.CATEGORY.getName() + " VARCHAR(100) DEFAULT '',"
				+ LogFieldName.CLASS.getName() + " VARCHAR(100) DEFAULT '',"
				+ LogFieldName.DATE.getName() + " VARCHAR(100) DEFAULT '',"
				+ LogFieldName.FILE.getName() + " VARCHAR(100) DEFAULT '',"
				+ LogFieldName.LOCATION_INFO.getName() + " VARCHAR(100) DEFAULT '',"
				+ LogFieldName.LINE.getName() + " VARCHAR(100) DEFAULT '',"
				+ LogFieldName.METHOD.getName() + " VARCHAR(100) DEFAULT '',"
				+ LogFieldName.LEVEL.getName() + " VARCHAR(100) DEFAULT '',"
				+ LogFieldName.MILLISECONDS.getName() + " VARCHAR(100) DEFAULT '',"
				+ LogFieldName.THREAD.getName() + " VARCHAR(100) DEFAULT '',"
				+ LogFieldName.MESSAGE.getName() + " VARCHAR(1000) DEFAULT '',"
				+ LogFieldName.THROWABLE.getName() + " VARCHAR(MAX) DEFAULT ''"
				+ ")";
		DaoUtil.executeQuery(createTableQueryString);
		logger.info("Logs table was created");
	}

	private void createLogsInsTrigger() {
		String createTriggerQueryString = "CREATE TRIGGER IF NOT EXISTS logs_ins AFTER INSERT ON logs "
				+ "FOR EACH ROW CALL \"com.rdiachenko.jlv.log4j.dao.LogsInsTrigger\"";
		DaoUtil.executeQuery(createTriggerQueryString);
		logger.info("Logs table insertion trigger was created");
	}
}
