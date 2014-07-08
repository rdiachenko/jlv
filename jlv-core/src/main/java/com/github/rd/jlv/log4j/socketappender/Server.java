package com.github.rd.jlv.log4j.socketappender;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;

public class Server extends Thread {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	private final EventBus eventBus = new EventBus();

	private ServerSocket serverSocket;

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

	@Override
	public void run() {
		logger.debug("Server was started");

		while (!serverSocket.isClosed()) {
			try {
				logger.debug("Waiting for a new connection");
				final Socket socket = serverSocket.accept();
				socket.setSoTimeout(5000);
				logger.debug("Connection has been accepted from " + socket.getInetAddress().getHostName());
				executor.execute(new SocketLogHandler(socket, eventBus));
			} catch (RejectedExecutionException e) {
				if (!executor.isShutdown()) {
					logger.warn("Connections submission rejected", e);
				}
			} catch (IOException e) {
				logger.warn("Failed to accept a new connection: server socket was closed.");
			}
		}
	}

	public void shutdown() {
		try {
			serverSocket.close();
			logger.debug("Server was stopped");
		} catch (IOException e) {
			logger.error("IOException occurred while closing server socket:", e);
		} finally {
			try {
				executor.shutdown();

				if (!executor.awaitTermination(Integer.MAX_VALUE, TimeUnit.NANOSECONDS)) {
					executor.shutdownNow();
				}
			} catch (InterruptedException e) {
				logger.error("InterruptedException occurred while shutting down server executor.", e);
			}
		}
	}

	public void addLogEventListener(Object listener) {
		if (listener != null) {
			eventBus.register(listener);
		}
	}

	public void removeLogEventListener(Object listener) {
		if (listener != null) {
			eventBus.unregister(listener);
		}
	}
}
