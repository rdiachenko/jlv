package com.rdiachenko.jlv.log4j.dao;

import java.sql.Connection;
import java.sql.SQLException;

import org.h2.jdbcx.JdbcConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionPool {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private JdbcConnectionPool connectionPool;

	public ConnectionPool() {
		this("org.h2.Driver", "jdbc:h2:src/main/resources/jlv", "jlv", "jlv");
	}

	public ConnectionPool(String dbDriver, String dbUrl, String user, String password) {
		try {
			Class.forName(dbDriver);
			connectionPool = JdbcConnectionPool.create(dbUrl, user, password);
		} catch (ClassNotFoundException e) {
			logger.error("Db driver could not be found" + dbDriver, e);
			throw new IllegalStateException("Db driver could not be found: " + dbDriver, e);
		}
	}

	public Connection getConnection() {
		try {
			return connectionPool.getConnection();
		} catch (SQLException e) {
			logger.error("Exception occurs while getting connection from the connection pool:", e);
			throw new IllegalStateException("Exception occurs while getting connection from the connection pool:", e);
		}
	}

	public void dispose() {
		connectionPool.dispose();
	}
}
