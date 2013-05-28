package com.rdiachenko.jlv.log4j.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rdiachenko.jlv.log4j.domain.Log;
import com.rdiachenko.jlv.log4j.domain.LogContainer;

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
				Log log = (new Log.Builder()).categoryName(result.getString("category"))
						.className(result.getString("class"))
						.date(result.getString("date"))
						.fileName(result.getString("file"))
						.locationInfo(result.getString("locInfo"))
						.lineNumber(result.getString("line"))
						.methodName(result.getString("method"))
						.level(result.getString("level"))
						.ms(result.getString("ms"))
						.threadName(result.getString("thread"))
						.message(result.getString("message"))
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
				+ "(category, class, date, file, locInfo, line, method, level, ms, thread, message) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
				+ "category VARCHAR(100) DEFAULT '',"
				+ "class VARCHAR(100) DEFAULT '',"
				+ "date VARCHAR(100) DEFAULT '',"
				+ "file VARCHAR(100) DEFAULT '',"
				+ "locInfo VARCHAR(100) DEFAULT '',"
				+ "line VARCHAR(100) DEFAULT '',"
				+ "method VARCHAR(100) DEFAULT '',"
				+ "level VARCHAR(100) DEFAULT '',"
				+ "ms VARCHAR(100) DEFAULT '',"
				+ "thread VARCHAR(100) DEFAULT '',"
				+ "message VARCHAR(1000) DEFAULT '',"
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
