package com.rdiachenko.jlv.log4j.dao;

import java.sql.Connection;
import java.sql.SQLException;

import org.h2.jdbcx.JdbcConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ConnectionPool {

	CONNECTION_POOL("org.h2.Driver", "jdbc:h2:src/main/resources/jlv", "jlv", "jlv");

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private JdbcConnectionPool connectionPool;

	private ConnectionPool(String dbDriver, String dbUrl, String user, String password) {
		try {
			Class.forName(dbDriver);
			connectionPool = JdbcConnectionPool.create(dbUrl, user, password);
		} catch (ClassNotFoundException e) {
			logger.error("Db driver could not be found" + dbDriver, e);
			throw new IllegalStateException("Db driver could not be found: " + dbDriver, e);
		}
	}

	public Connection getConnection() throws SQLException {
		return connectionPool.getConnection();
	}

	public void dispose() {
		connectionPool.dispose();
	}
}
