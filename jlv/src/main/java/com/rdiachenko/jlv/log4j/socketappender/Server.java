package com.rdiachenko.jlv.log4j.socketappender;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private ServerSocket serverSocket = null;

	private volatile boolean listening = true;

	public Server(int port) throws IOException {
		if (port < 0) {
			throw new IllegalArgumentException("Port value should be a positive number. Current port: " + port);
		}
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			throw new IOException("Could not listen on port: " + port, e);
		}
	}

	public void start() {
		logger.info("Server was started");

		while (listening) {
			try {
				Socket socket = serverSocket.accept();
				ClientThread client = new ClientThread(socket);
				client.run();
			} catch (IOException e) {
				logger.error("Socket couldn't be started:", e);
			}
		}
	}

	public void stop() {
		listening = false;
		try {
			serverSocket.close();
			logger.info("Server was stopped");
		} catch (IOException e) {
			logger.error("Server socket couldn't be closed:", e);
		}
	}
}
