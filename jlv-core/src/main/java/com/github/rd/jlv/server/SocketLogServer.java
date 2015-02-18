package com.github.rd.jlv.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * The class represents a server which accepts socket connections. For each accepted connection it creates and runs a
 * separate thread for handling the incoming events.
 *
 * @author <a href="mailto:rd.ryly@gmail.com">Ruslan Diachenko</a>
 */
public class SocketLogServer extends Server {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final int SOCKET_TIMEOUT = 5000; // ms

	private final ExecutorService bossExecutor = Executors.newSingleThreadExecutor();
	private final ExecutorService workerExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	
	private ServerSocket serverSocket;

	public SocketLogServer(int port) {
		Preconditions.checkArgument(port >= 0, "Port value must be a positive number. Current port: %s", port);

		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			logger.error("Could not open socket on port: {}", port, e);
		}
	}

	@Override
	public void start() {
		bossExecutor.execute(new Runnable() {
			@Override
			public void run() {
				while (!serverSocket.isClosed()) {
					try {
						logger.debug("Waiting for a new connection");
						final Socket socket = serverSocket.accept();
						socket.setSoTimeout(SOCKET_TIMEOUT);
						logger.debug("Connection has been accepted from " + socket.getInetAddress().getHostName());
						runHandler(new SocketLogHandler(socket, eventBus));
					} catch (IOException e) {
						logger.warn("Failed to accept a new connection: server socket was closed.");
					}
				}
			}
		});
	}

	@Override
	public void stop() {
		try {
			serverSocket.close();
			logger.debug("Server socket closed.");
		} catch (IOException e) {
			logger.error("Server stop failed.", e);
		}
		shutdownExecutor(workerExecutor);
		shutdownExecutor(bossExecutor);
	}
	
	private void runHandler(Runnable runnable) {
		if (runnable != null) {
			try {
				workerExecutor.execute(runnable);
			} catch (RejectedExecutionException e) {
				if (!workerExecutor.isShutdown()) {
					logger.warn("Connections submission rejected", e);
				}
			}
		}
	}
	
	private void shutdownExecutor(ExecutorService executor) {
		try {
			executor.shutdown();

			if (!executor.awaitTermination(Integer.MAX_VALUE, TimeUnit.NANOSECONDS)) {
				executor.shutdownNow();
			}
		} catch (InterruptedException e) {
			logger.error("Executor shutdown failed.", e);
		}
	}
}
