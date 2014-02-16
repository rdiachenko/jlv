package com.github.rd.jlv.log4j.socketappender;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server extends Thread {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private ServerSocket serverSocket;

	private volatile boolean listening = true;

	private BlockingQueue<ClientThread> clients = new LinkedBlockingQueue<>();

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

		while (listening) {
			try {
				logger.debug("Waiting for a new connection");
				Socket clientSocket = serverSocket.accept();
				logger.debug("Connection has been accepted from " + clientSocket.getInetAddress().getHostName());
				ClientThread client = new ClientThread(clientSocket);

				if (clients.offer(client)) {
					Thread clientThread = new Thread(client);
					clientThread.setDaemon(true);
					clientThread.start();
				}
			} catch (IOException e) {
				logger.warn("Failed to accept a new connection: server socket was closed.");
			}
		}
	}

	public void shutdown() {
		listening = false;

		try {
			ClientThread client = clients.poll();

			while (client != null) {
				client.shutdown();
				client = clients.poll();
			}
		} finally {
			try {
				serverSocket.close();
				logger.debug("Server was stopped");
			} catch (IOException e) {
				logger.error("IOException occurred while closing server socket:", e);
			}
		}
	}
}
