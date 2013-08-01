package com.rdiachenko.jlv.log4j.socketappender;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import org.apache.log4j.EnhancedPatternLayout;
import org.apache.log4j.Level;
import org.apache.log4j.jdbc.JDBCAppender;
import org.apache.log4j.spi.LoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rdiachenko.jlv.log4j.dao.ConnectionFactory;
import com.rdiachenko.jlv.log4j.domain.LogFieldName;

public class ClientThread {

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

			while (!socket.isClosed()) {
				LoggingEvent log = (LoggingEvent) inputStream.readObject();
				dbAppender.append(log);
			}

		} catch (EOFException e) {
			// When the client closes the connection, the stream will run out of data, and the ObjectInputStream.readObject method will throw the exception
//			logger.warn("Exception occur while reading object from socket input stream:", e);
		} catch (IOException e) {
			logger.error("Errors occur while reading from socket's input stream:", e);

		} catch (ClassNotFoundException e) {
			logger.error("", e);

		} finally {
			stop();
		}
	}

	public void stop() {
		try {
			if (!socket.isClosed()) {
				logger.info("Closing client's socket...");
				socket.close();
				logger.info("Client's socket was closed");
			}
		} catch (IOException e) {
			logger.error("Socket could not be closed: ", e);
		}
	}

//	private final static class Log4jFileAppender {
//
//		private static final String LOG_FILE = "src/main/resources/log4j.log";
//		private static final String PATTERN_LAYOUT = "[%c][%C][%d][%F][%l][%L][%M][%p][%r][%t][%m]%n";
//
//		private final FileAppender fileAppender;
//
//		private Log4jFileAppender() {
//			fileAppender = new FileAppender();
//			fileAppender.setFile(LOG_FILE);
//			fileAppender.setLayout(new PatternLayout(PATTERN_LAYOUT));
//			fileAppender.setThreshold(Level.ALL);
//			fileAppender.setAppend(false);
//			fileAppender.activateOptions();
//		}
//
//		private void append(LoggingEvent le) {
//			fileAppender.append(le);
//		}
//	}

	private final static class Log4jDbAppender {

		private static final String SQL = "INSERT INTO logs ("
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
				+ "VALUES ('%c', '%C', '%d', '%F', '%l', '%L', '%M', '%p', '%r', '%t', '%m', '%throwable')";

		private final JDBCAppender jdbcAppender;

		private Log4jDbAppender() {
			jdbcAppender = new JDBCAppender();
			jdbcAppender.setDriver(ConnectionFactory.CONNECTION.getDbDriver());
			jdbcAppender.setURL(ConnectionFactory.CONNECTION.getDbUrl());
			jdbcAppender.setUser(ConnectionFactory.CONNECTION.getUser());
			jdbcAppender.setPassword(ConnectionFactory.CONNECTION.getPassword());
			jdbcAppender.setSql(SQL);
			jdbcAppender.setLocationInfo(true);
			jdbcAppender.setThreshold(Level.ALL);
			jdbcAppender.activateOptions();
			jdbcAppender.setLayout(new EnhancedPatternLayout(SQL));
		}

		private void append(LoggingEvent le) {
			jdbcAppender.append(le);
		}
	}
}
