package com.github.rd.jlv.log4j.socketappender;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import org.apache.log4j.spi.LoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rd.jlv.log4j.LogUtils;
import com.github.rd.jlv.log4j.domain.Log;
import com.github.rd.jlv.log4j.domain.LogEventContainer;

public class SocketLogHandler implements Runnable {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final Socket socket;

	public SocketLogHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		ObjectInputStream objectStream = null;
		BufferedInputStream inputStream = null;

		try {
			inputStream = new BufferedInputStream(socket.getInputStream());
			objectStream = new ObjectInputStream(inputStream);

			while (!socket.isClosed()) {
				LoggingEvent log = (LoggingEvent) objectStream.readObject();
				send(log);
			}
		} catch (EOFException e) {
			// When the client closes the connection, the stream will run out of data, 
			// and the ObjectInputStream.readObject method will throw the exception
			logger.info("Reached EOF, closing client's connection");
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			logger.error("ClassNotFoundException occurred while reading LoggingEvent", e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error("IOException occurred while closing input stream", e);
				}
			}

			if (objectStream != null) {
				try {
					objectStream.close();
				} catch (IOException e) {
					logger.error("IOException occurred while closing object stream", e);
				}
			}
			shutdown();
		}
	}

	private void shutdown() {
		try {
			logger.debug("Closing socket");
			socket.close();
			logger.debug("Socket was closed");
		} catch (IOException e) {
			logger.error("IOException occurred while closing client's connection", e);
		}
	}

	private void send(LoggingEvent le) {
		Log log = LogUtils.convert(le);
		LogEventContainer.notifyListeners(log);
	}
}
