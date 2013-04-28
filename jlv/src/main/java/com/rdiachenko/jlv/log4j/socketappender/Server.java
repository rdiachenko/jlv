package com.rdiachenko.jlv.log4j.socketappender;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final ExecutorService executor = Executors.newSingleThreadExecutor();

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
			executor.execute(new ClientThread(serverSocket.accept()));
		}
	}

	public void stop() throws IOException {
		listening = false;
		try {
			executor.shutdown();
			if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
				executor.shutdownNow();
			}
		} catch (InterruptedException e) {
			logger.error("Client's thread proccessing was interrupted: ", e);
		} finally {
			serverSocket.close();
		}
	}
}
