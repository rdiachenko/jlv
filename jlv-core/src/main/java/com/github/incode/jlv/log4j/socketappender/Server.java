package com.github.incode.jlv.log4j.socketappender;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server extends Thread {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private ServerSocket serverSocket;

	private volatile boolean listening = true;

	private List<ClientThread> clients = Collections.synchronizedList(new ArrayList<ClientThread>());

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

	@Override
	public void run() {
		logger.debug("Server was started");

		try {
			while (listening) {
				logger.debug("Waiting for a new connection");
				Socket clientSocket = serverSocket.accept();
				logger.debug("Connection has been accepted from " + clientSocket.getInetAddress().getHostName());
				ClientThread client = new ClientThread(clientSocket);
				clients.add(client);
				Thread clientThread = new Thread(client);
				clientThread.setDaemon(true);
				clientThread.start();
			}
		} catch (IOException e) {
			logger.error("Error in accepting a new connection:", e);
		}
	}

	public void stopServer() {
		listening = false;
		synchronized (clients) {
			for (ClientThread client : clients) {
				client.stopClient();
			}
		}
		try {
			serverSocket.close();
			logger.debug("Server was stopped");
		} catch (IOException e) {
			logger.error("Error while closing server socket:", e);
		}
	}
}
