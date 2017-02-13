package com.rdiachenko.jlv.plugin.view;

import com.google.common.eventbus.Subscribe;
import com.rdiachenko.jlv.CircularBuffer;
import com.rdiachenko.jlv.Log;
import com.rdiachenko.jlv.SocketLogServer;

public class LogListViewController {
    
    private final CircularBuffer<Log> input;
    private final LogCollector logCollector;
    private final SocketLogServer server;
    private final LogListViewRefresher viewRefresher;
    
    public LogListViewController(LogListView view) {
        input = new CircularBuffer<>(10000);
        logCollector = new LogCollector(input);
        server = new SocketLogServer(7777);
        server.addLogEventListener(logCollector);
        viewRefresher = new LogListViewRefresher(view);
    }

    public CircularBuffer<Log> getInput() {
        return input;
    }

    public void startServer() {
        server.start();
    }

    public void stopServer() {
        server.stop();
    }
    
    public void startViewRefresher() {
        viewRefresher.start(2000);
    }
    
    public void stopViewRefresher() {
        viewRefresher.stop();
    }

    public void dispose() {
        server.stop();
        viewRefresher.stop();
    }
    
    private final class LogCollector {

        private CircularBuffer<Log> buffer;

        public LogCollector(CircularBuffer<Log> buffer) {
            this.buffer = buffer;
        }

        @Subscribe
        public void handle(Log log) {
            buffer.add(log);
        }
    }
}
