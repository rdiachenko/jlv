package com.rdiachenko.jlv.log4j.socketappender;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

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

	public void start() throws IOException {
		while (listening) {
			Socket socket = serverSocket.accept();
			ClientThread client = new ClientThread(socket);
			client.run();
		}
	}

	public void stop() throws IOException {
		listening = false;
		serverSocket.close();
	}
}
