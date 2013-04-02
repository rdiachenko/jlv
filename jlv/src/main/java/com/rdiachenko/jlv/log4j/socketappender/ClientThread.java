package com.rdiachenko.jlv.log4j.socketappender;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import org.apache.log4j.spi.LoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rdiachenko.jlv.io.TextFileSaver;

public class ClientThread implements Runnable {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private Socket socket = null;
	
	public ClientThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		File file = new File("src/main/resources/log.log");
		TextFileSaver fileSaver = null;
		ObjectInputStream inputStream = null;

		try {
			fileSaver = new TextFileSaver(file);
			inputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			Object object = null;

			while ((object = inputStream.readObject()) != null) {
				LoggingEvent log = (LoggingEvent) object;
				fileSaver.sendTextToFile(logAsString(log));
			}

		} catch (FileNotFoundException e) {
			logger.error("Errors occur while creating|modifying file: " + file.getName(), e);
			
		} catch (IOException e) {
			logger.error("Errors occur while reading from socket's input stream: ", e);

		} catch (ClassNotFoundException e) {
			logger.error("Couldn't recognized LoggingEvent object from the socket's input stream: ", e);

		} finally {
			if (fileSaver != null) {
				fileSaver.flushAndClose();
			}
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
	
	private String logAsString(LoggingEvent le) {
		StringBuilder log = new StringBuilder();
		
		if (le != null) {
			log.append(le.getMessage());
		}
		return log.toString();
	}
}
