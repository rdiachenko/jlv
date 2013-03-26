package com.rdiachenko.jlv.log4j.socketappender;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {

	private ServerSocket serverSocket = null;
	
	private boolean listening = true;
	
	public Server(int port) throws IOException {
		if (port < 0) {
			throw new IllegalArgumentException("Port value should be a positive number. Current port: " + port);
		}
		try {
			serverSocket = new ServerSocket(port);
		} catch(IOException e) {
			throw new IOException("Could not listen on port: " + port, e);
		}
	}
	
	public void start() {
		while (listening) {
			// new ClientThread(serverSocket.accept()).start();
		}
	}
	
	public void stop() throws IOException {
		serverSocket.close();
	}
}
