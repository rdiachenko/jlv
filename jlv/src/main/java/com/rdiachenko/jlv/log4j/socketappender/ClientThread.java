package com.rdiachenko.jlv.log4j.socketappender;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.jdbc.JDBCAppender;
import org.apache.log4j.spi.LoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientThread implements Runnable {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private Socket socket = null;

	public ClientThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
//		Log4jFileAppender fileAppender = new Log4jFileAppender();
		Log4jDbAppender dbAppender = null;
		ObjectInputStream inputStream = null;

		try {
			dbAppender = new Log4jDbAppender();
			inputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			Object object = null;

			while ((object = inputStream.readObject()) != null) {
				LoggingEvent log = (LoggingEvent) object;
				dbAppender.append(log);
			}

		} catch (InstantiationException e) {
			logger.error("", e);

		} catch (IllegalAccessException e) {
			logger.error("", e);

		} catch (SQLException e) {
			logger.error("", e);

		} catch (EOFException e) {
			// When the client closes the connection, the stream will run out of data, and the ObjectInputStream.readObject method will throw the exception
			// TODO: create more accurate handling
			logger.warn("Exception occur while reading object from socket input stream:", e);

		} catch (IOException e) {
			logger.error("Errors occur while reading from socket's input stream:", e);

		} catch (ClassNotFoundException e) {
			logger.error("", e);

		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				logger.error("Socket's input stream could not be closed:", e);
			}
			try {
				socket.close();
			} catch (IOException e) {
				logger.error("Socket could not be closed: ", e);
			}
		}
	}

	private final static class Log4jFileAppender {

		private static final String LOG_FILE = "src/main/resources/log4j.log";
		private static final String PATTERN_LAYOUT = "[%c][%C][%d][%F][%l][%L][%M][%p][%r][%t][%m]%n";

		private final FileAppender fileAppender;

		private Log4jFileAppender() {
			fileAppender = new FileAppender();
			fileAppender.setFile(LOG_FILE);
			fileAppender.setLayout(new PatternLayout(PATTERN_LAYOUT));
			fileAppender.setThreshold(Level.ALL);
			fileAppender.setAppend(false);
			fileAppender.activateOptions();
		}

		private void append(LoggingEvent le) {
			fileAppender.append(le);
		}
	}

	private final static class Log4jDbAppender {

		private static final String DB_DRIVER = "org.h2.Driver";
		private static final String DB_URL = "jdbc:h2:src/main/resources/jlv";
		private static final String USER_NAME = "jlv";
		private static final String USER_PASSWORD = "jlv";
		private static final String SQL = "INSERT INTO logs (category, class, date, file, locInfo, line, method, level, ms, thread, message) "
				+ "VALUES ('%c', '%C', '%d', '%F', '%l', '%L', '%M', '%p', '%r', '%t', '%m')";

		private final JDBCAppender jdbcAppender;

		private Log4jDbAppender() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
			initDb();
			jdbcAppender = new JDBCAppender();
			jdbcAppender.setDriver(DB_DRIVER);
			jdbcAppender.setURL(DB_URL);
			jdbcAppender.setUser(USER_NAME);
			jdbcAppender.setPassword(USER_PASSWORD);
			jdbcAppender.setSql(SQL);
			jdbcAppender.setLocationInfo(true);
			jdbcAppender.setThreshold(Level.ALL);
			jdbcAppender.activateOptions();
		}

		private static void initDb() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
			Class.forName(DB_DRIVER).newInstance();
			Connection conn = null;

			try {
				conn = DriverManager.getConnection(DB_URL, USER_NAME, USER_PASSWORD);
				Statement statement = conn.createStatement();
				statement.execute("DROP TABLE logs IF EXISTS");
				statement.execute("CREATE TABLE logs("
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
						+ ")");
			} finally {
				conn.close();
			}
		}

		private void append(LoggingEvent le) {
			jdbcAppender.append(le);
		}
	}
}
