package com.rdiachenko.jlv;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rdiachenko.jlv.converter.LogConverterType;

public class SocketConnectionHandler implements Runnable {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private final Socket socket;

    private final Set<LogEventListener> logEventListeners;
    
    public SocketConnectionHandler(Socket socket) {
        this(socket, new HashSet<>());
    }

    public SocketConnectionHandler(Socket socket, Set<LogEventListener> logEventListeners) {
        if (socket == null) {
            throw new NullPointerException("Socket is null");
        }
        this.socket = socket;

        if (logEventListeners == null) {
            throw new NullPointerException("Log event listeners collection is null");
        }
        this.logEventListeners = new HashSet<>(logEventListeners);
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
                
                for (LogEventListener listener : logEventListeners) {
                    listener.handleLog(log);
                }
            }
        } catch (EOFException e) {
            // When client closes connection, the stream will run out of data,
            // and the ObjectInputStream.readObject method will throw the exception
            logger.warn("Reached EOF while reading from socket");
        } catch (IOException | ClassNotFoundException e) {
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
