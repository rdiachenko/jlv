package com.rdiachenko.jlv.log4j.socketappender;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import org.apache.log4j.spi.LoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rdiachenko.jlv.log4j.domain.LogContainer;

public class ClientThread implements Runnable {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private Socket socket = null;
	
	private LogContainer container = null;

	public ClientThread(Socket socket) {
		this.socket = socket;
		container = LogContainer.createNewContainer();
	}

	public void run() {
		ObjectInputStream inputStream = null;

		try {
			inputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			Object object = null;

			while ((object = inputStream.readObject()) != null) {
				LoggingEvent log = (LoggingEvent) object;
				container.add(log);
			}

		} catch (IOException e) {
			logger.error("Errors occur while reading from socket's input stream: ", e);

		} catch (ClassNotFoundException e) {
			logger.error("Couldn't recognized LoggingEvent object from the socket's input stream: ", e);

		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				logger.error("Socket's input stream could not be closed: ", e);
			}
			try {
				socket.close();
			} catch (IOException e) {
				logger.error("Socket could not be closed: ", e);
			}
		}
	}
}
