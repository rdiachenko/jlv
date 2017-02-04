package com.rdiachenko.jlv;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketLogServer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final int SOCKET_TIMEOUT_MS = 10000;
    private static final int EXECUTOR_TIMEOUT_MS = 30000;

    private final ExecutorService mainExecutor;
    private final ExecutorService workerExecutor;

    private final ServerSocket serverSocket;

    private final Set<LogEventListener> logEventListeners = new HashSet<>();

    public SocketLogServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not open socket on port: " + port);
        }
        mainExecutor = Executors.newSingleThreadExecutor();
        workerExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public void addLogEventListener(LogEventListener listener) {
        if (logEventListeners.add(listener)) {
            logger.info("Log event listener {} added", listener);
        }
    }

    public void removeLogEventListener(LogEventListener listener) {
        if (logEventListeners.remove(listener)) {
            logger.info("Log event listener {} removed", listener);
        }
    }

    public void start() {
        logger.info("Starting socket server");
        mainExecutor.execute(new Runnable() {
            @Override
            public void run() {
                while (!serverSocket.isClosed()) {
                    try {
                        logger.info("Waiting for a new connection...");
                        Socket socket = serverSocket.accept();
                        socket.setSoTimeout(SOCKET_TIMEOUT_MS);
                        logger.info("Connection has been accepted from {}", socket.getInetAddress());
                        SocketConnectionHandler connectionHandler = new SocketConnectionHandler(socket,
                                logEventListeners);
                        executeConnectionHandler(connectionHandler);
                    } catch (Exception e) {
                        logger.warn("Failed to accept a new connection: {}", e.getLocalizedMessage());
                    }
                }
            }
        });
        logger.info("Socket server started");
    }

    public void stop() {
        logger.info("Stopping socket server");
        try {
            serverSocket.close();
            logger.info("Socket server stopped");
        } catch (IOException e) {
            logger.error("Failed to stop socket server", e);
        } finally {
            shutdownExecutor(workerExecutor);
            shutdownExecutor(mainExecutor);
        }
    }

    private void shutdownExecutor(ExecutorService executor) {
        logger.info("Shutting down executor {}", executor);
        try {
            executor.shutdown();

            if (!executor.awaitTermination(EXECUTOR_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
            logger.info("Executor shut down: {}", executor.isTerminated());
        } catch (Exception e) {
            logger.error("Failed to shutdown executor {}", executor, e);
        }
    }

    private void executeConnectionHandler(Runnable runnable) {
        try {
            workerExecutor.execute(runnable);
        } catch (RejectedExecutionException e) {
            if (!workerExecutor.isShutdown()) {
                logger.error("Connection handler is rejected for execution", e);
            }
        }
    }
}
