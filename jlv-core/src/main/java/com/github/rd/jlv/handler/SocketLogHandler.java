package com.github.rd.jlv.handler;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;

/**
 * The main goal of this class is to read incoming log events, convert them and send to EventBus.
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
		try (BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream());
				ObjectInputStream objectStream = new ObjectInputStream(inputStream)) {

			LogConverter logConverter = null;

			while (!socket.isClosed()) {
				Object log = objectStream.readObject();

				if (logConverter == null) {
					logConverter = LogType.typeOf(log).converter();
				}
				eventBus.post(logConverter.convert(log));
			}
		} catch (EOFException e) {
			// When the client closes the connection, the stream will run out of data,
			// and the ObjectInputStream.readObject method will throw the exception
			logger.warn("Reached EOF while reading from socket.");
		} catch (IOException | ClassNotFoundException e) {
			logger.error("Couldn't handle input stream.", e);
		} finally {
			shutdown();
		}
	}

	private void shutdown() {
		try {
			socket.close();
			logger.debug("Socket closed.");
		} catch (IOException e) {
			logger.error("Socket close failed.", e);
		}
	}
}
