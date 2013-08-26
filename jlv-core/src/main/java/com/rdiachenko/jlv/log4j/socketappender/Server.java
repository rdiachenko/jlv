package com.rdiachenko.jlv.log4j.socketappender;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server extends Thread {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private ServerSocket serverSocket;

	private volatile boolean listening = true;

	public Server(int port) throws IOException {
		if (port < 0) {
			throw new IllegalArgumentException("Port value should be a positive number. Current port: " + port);
		}
		try {
			setDaemon(true);
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			throw new IOException("Could not listen on port: " + port, e);
		}
	}

	public void runServer() {
		logger.debug("Server was started");

		try {
			while (listening) {
				logger.debug("Waiting for a new connection");
				Socket client = serverSocket.accept();
				logger.debug("Connection has been accepted from " + client.getInetAddress().getHostName());
				Thread clientThread = new Thread(new ClientThread(client));
				clientThread.setDaemon(true);
				clientThread.start();
			}
		} catch (IOException e) {
			logger.error("Error in accepting a new connection:", e);
		}
	}

	public void stopServer() {
		listening = false;
		try {
			serverSocket.close();
			logger.debug("Server was stopped");
		} catch (IOException e) {
			logger.error("Error while closing server socket:", e);
		}
	}
}
