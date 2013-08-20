package com.rdiachenko.jlv.log4j.socketappender;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import org.apache.log4j.spi.LoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rdiachenko.jlv.log4j.LogConverter;
import com.rdiachenko.jlv.log4j.dao.DaoProvider;
import com.rdiachenko.jlv.log4j.dao.LogDao;
import com.rdiachenko.jlv.log4j.domain.Log;
import com.rdiachenko.jlv.log4j.domain.LogEventContainer;

public class ClientThread {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private Socket socket;

	private LogDao logDao;

	public ClientThread(Socket socket) {
		this.socket = socket;
		logDao = DaoProvider.LOG_DAO.getLogDao();
	}

	public void run() {
		ObjectInputStream inputStream = null;

		try {
			inputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));

			while (!socket.isClosed()) {
				LoggingEvent log = (LoggingEvent) inputStream.readObject();
				append(log);
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
		LogEventContainer.endLogEvent();
	}

	private void append(LoggingEvent le) {
		Log log = LogConverter.convert(le);
		logDao.insert(log);
		LogEventContainer.notifyListeners(log);
	}
}
