package com.rdiachenko.jlv;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;
import com.rdiachenko.jlv.converter.LogConverterType;

public class SocketConnectionHandler implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Socket socket;

    private final EventBus eventBus;

    public SocketConnectionHandler(Socket socket, EventBus eventBus) {
        Preconditions.checkNotNull(socket, "Socket is null");
        Preconditions.checkNotNull(eventBus, "Event bus is null");
        this.socket = socket;
        this.eventBus = eventBus;
    }

    @Override
    public void run() {
        try (BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream());
                ObjectInputStream objectStream = new ObjectInputStream(inputStream)) {

            LogConverterType converterType = null;

            while (!socket.isClosed()) {
                Object obj = objectStream.readObject();

                if (converterType == null) {
                    converterType = LogConverterType.valueOf(obj);
                    logger.info("Log converter type detected: {}", converterType.name());
                }
                Log log = converterType.convert(obj);
                eventBus.post(log);
            }
        } catch (EOFException e) {
            // When client closes connection, the stream will run out of data,
            // and the ObjectInputStream.readObject method will throw the exception
            logger.warn("Reached EOF while reading from socket");
        } catch (Exception e) {
            logger.error("Failed to handle input stream", e);
        } finally {
            stop();
        }
    }

    private void stop() {
        logger.info("Stopping connection handler");
        try {
            socket.close();
            logger.info("Connection handler stopped");
        } catch (IOException e) {
            logger.error("Failed to stop connection handler", e);
        }
    }
}
