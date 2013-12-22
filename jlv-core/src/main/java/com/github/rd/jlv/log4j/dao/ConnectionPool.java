package com.github.rd.jlv.log4j.dao;

import java.sql.Connection;
import java.sql.SQLException;

import org.h2.jdbcx.JdbcConnectionPool;

public class ConnectionPool {

	private JdbcConnectionPool connectionPool;

	public ConnectionPool() {
		this("org.h2.Driver", "jdbc:h2:src/main/resources/jlv", "jlv", "jlv");
	}

	public ConnectionPool(String dbDriver, String dbUrl, String user, String password) {
		try {
			Class.forName(dbDriver);
			connectionPool = JdbcConnectionPool.create(dbUrl, user, password);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Db driver could not be found: " + dbDriver, e);
		}
	}

	public Connection getConnection() {
		try {
			return connectionPool.getConnection();
		} catch (SQLException e) {
			throw new IllegalStateException("Exception occurs while getting connection from the connection pool:", e);
		}
	}

	public void dispose() {
		connectionPool.dispose();
	}
}
