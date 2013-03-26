package com.rdiachenko.jlv.log4j.socketappender;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientThread extends Thread {

	private Socket socket = null;

	public ClientThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		ObjectInputStream inputStream = null;

		try {
			inputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			Object object = null;

			while ((object = inputStream.readObject()) != null) {
				LoggingEvent log = (LoggingEvent) object;
				sendLogToList(log);
			}

		} catch (IOException e) {
			// log error

		} catch (ClassNotFoundException e) {
			// log error "Couldn't recognized LoggingEvent object from input stream";

		} finally {
			try {
				inputStream.close();
				socket.close();
			} catch (IOException e) {
				// log error Errors occur when closing socket
			}
		}
	}
}
