package com.github.rd.jlv.log4j.socketappender;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import org.apache.log4j.spi.LoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rd.jlv.log4j.LogUtil;
import com.github.rd.jlv.log4j.domain.Log;
import com.github.rd.jlv.log4j.domain.LogEventContainer;

public class ClientThread implements Runnable {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private Socket client;

	public ClientThread(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		try {
			ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream());

			while (!client.isClosed()) {
				LoggingEvent log = (LoggingEvent) inputStream.readObject();
				append(log);
			}

		} catch (EOFException e) {
			// When the client closes the connection, the stream will run out of data, and the ObjectInputStream.readObject method will throw the exception
			logger.info("Reached EOF, closing client's connection");
		} catch (IOException e) {
			logger.error("IOException occurs, closing client's connection:", e);
		} catch (ClassNotFoundException e) {
			logger.error("ClassNotFoundException occurs, closing client's connection:", e);
		} finally {
			shutdown();
		}
	}

	public void shutdown() {
		try {
			if (!client.isClosed()) {
				logger.debug("Closing client's connection...");
				client.close();
				logger.debug("Client's connection was closed");
			}
		} catch (IOException e) {
			logger.error("IOException occurs while closing client's connection:", e);
		}
	}

	private void append(LoggingEvent le) {
		Log log = LogUtil.convert(le);
		LogEventContainer.notifyListeners(log);
	}
}
