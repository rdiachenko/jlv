package com.github.rd.jlv.log4j.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DaoUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(DaoUtils.class);

	private DaoUtils() {
		throw new IllegalStateException("This is an util class. The object should not be created.");
	}

	public static void executeQuery(Connection conn, String query) {
		Statement statement = null;

		try {
			statement = conn.createStatement();
			statement.execute(query);
		} catch (SQLException e) {
			LOGGER.error("", e);
		} finally {
			close(conn, statement);
		}
	}

	public static void close(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.error("Connection could not be closed:", e);
			}
		}
	}

	public static void close(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				LOGGER.error("Statement could not be closed:", e);
			}
		}
	}

	public static void close(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				LOGGER.error("ResultSet could not be closed:", e);
			}
		}
	}

	public static void close(Connection connection, Statement statement) {
		close(statement);
		close(connection);
	}

	public static void close(Connection connection, Statement statement, ResultSet resultSet) {
		close(resultSet);
		close(statement);
		close(connection);
	}
}
