package com.github.rd.jlv.log4j.socketappender;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rd.jlv.server.AbstractServer;

/**
 * The class represents a server which accepts socket connections. For each accepted connection it creates and runs a
 * separate thread for handling the incoming events.
 *
 * @author <a href="mailto:rd.ryly@gmail.com">Ruslan Diachenko</a>
 */
public class SocketLogServer extends AbstractServer {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private ServerSocket serverSocket;

	public SocketLogServer(int port) throws IOException {
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
		while (!serverSocket.isClosed()) {
			try {
				logger.debug("Waiting for a new connection");
				final Socket socket = serverSocket.accept();
				socket.setSoTimeout(5000);
				logger.debug("Connection has been accepted from " + socket.getInetAddress().getHostName());
				execute(new SocketLogHandler(socket, getEventBus()));
			} catch (IOException e) {
				logger.warn("Failed to accept a new connection: server socket was closed.");
			}
		}
	}

	@Override
	public void shutdown() {
		try {
			serverSocket.close();
			logger.debug("Server was stopped");
		} catch (IOException e) {
			logger.error("IOException occurred while closing server socket:", e);
		} finally {
			super.shutdown();
		}
	}
}
