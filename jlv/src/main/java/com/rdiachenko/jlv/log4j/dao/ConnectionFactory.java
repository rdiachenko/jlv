package com.rdiachenko.jlv.log4j.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ConnectionFactory {

	CONNECTION ("org.h2.Driver", "jdbc:h2:src/main/resources/jlv", "jlv", "jlv");

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private String dbDriver;
	private String dbUrl;
	private String user;
	private String password;

	private ConnectionFactory(String dbDriver, String dbUrl, String user, String password) {
		this.dbDriver = dbDriver;
		this.dbUrl = dbUrl;
		this.user = user;
		this.password = password;

		try {
			Class.forName(dbDriver);
		} catch (ClassNotFoundException e) {
			logger.error("Db driver could not be found:", e);
		}
	}

	public String getDbDriver() {
		return dbDriver;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public Connection getConnection() {
		Connection connection = null;

		try {
			connection = DriverManager.getConnection(dbUrl, user, password);
		} catch (SQLException e) {
			logger.error("Db connection could not be opened:", e);
		}
		return connection;
	}
}
