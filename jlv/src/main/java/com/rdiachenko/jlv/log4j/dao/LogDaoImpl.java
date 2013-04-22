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

	public LogContainer getAllLogs() {
//		String queryString = "SELECT * FROM logs";
//		Connection conn = ConnectionFactory.INSTANCE.getConnection();
//		PreparedStatement ps = conn.prepareStatement(queryString);
//		ps.setInt(1, value);
//		ResultSet rs = ps.executeQuery();
//		while (rs.next()) {
//
//		}
		return null;
	}

	public LogContainer getTailingLogs(int tail) {
		String queryString = "SELECT * FROM logs ORDER BY id DESC LIMIT ?";
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		LogContainer logs = new LogContainer();

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
			try {
				if (result != null) {
					result.close();
				}
			} catch (SQLException e) {
				logger.error("Result set could not be closed:", e);
			}

			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
			} catch (SQLException e) {
				logger.error("Prepared statement could not be closed:", e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.error("Connection could not be closed:", e);
			}
		}
		return logs;
	}

}
