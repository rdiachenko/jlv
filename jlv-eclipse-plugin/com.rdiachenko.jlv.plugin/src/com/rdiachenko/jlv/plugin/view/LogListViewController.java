package com.rdiachenko.jlv.plugin.view;

import org.eclipse.swt.widgets.Display;

import com.google.common.eventbus.Subscribe;
import com.rdiachenko.jlv.CircularBuffer;
import com.rdiachenko.jlv.Log;
import com.rdiachenko.jlv.SocketLogServer;

public class LogListViewController {
    
    private final LogListView view;
    private final CircularBuffer<Log> input;
    private final LogCollector logCollector;
    private final SocketLogServer server;
    
    public LogListViewController(LogListView view) {
        this.view = view;
        input = new CircularBuffer<>(10000);
        logCollector = new LogCollector(input);
        server = new SocketLogServer(7777);
        server.addLogEventListener(logCollector);
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
    
    private final class LogCollector {

        private CircularBuffer<Log> buffer;

        public LogCollector(CircularBuffer<Log> buffer) {
            this.buffer = buffer;
        }

        @Subscribe
        public void handle(Log log) {
            buffer.add(log);

            Display.getDefault().syncExec(new Runnable() {
                @Override
                public void run() {
                    view.refresh();
                }
            });
        }
    }
}
