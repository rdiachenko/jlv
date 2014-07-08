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
import com.google.common.eventbus.EventBus;

/**
 * The main goal of this class is to read incoming LoggingEvent events, convert them and send to EventBus.
 *
 * @author <a href="mailto:rd.ryly@gmail.com">Ruslan Diachenko</a>
 */
public class SocketLogHandler implements Runnable {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final Socket socket;

	private final EventBus eventBus;

	public SocketLogHandler(Socket socket, EventBus eventBus) {
		this.socket = socket;
		this.eventBus = eventBus;
	}

	@Override
	public void run() {
		ObjectInputStream objectStream = null;
		BufferedInputStream inputStream = null;

		try {
			inputStream = new BufferedInputStream(socket.getInputStream());
			objectStream = new ObjectInputStream(inputStream);

			while (!socket.isClosed()) {
				LoggingEvent le = (LoggingEvent) objectStream.readObject();
				Log log = LogUtils.convert(le);
				eventBus.post(log);
			}
		} catch (EOFException e) {
			// When the client closes the connection, the stream will run out of data,
			// and the ObjectInputStream.readObject method will throw the exception
			logger.warn("Reached EOF, closing client's connection");
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
			socket.close();
			logger.debug("Socket was closed");
		} catch (IOException e) {
			logger.error("IOException occurred while closing client's connection", e);
		}
	}
}
