package com.rdiachenko.jlv.plugin.view;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;
import com.rdiachenko.jlv.CircularBuffer;
import com.rdiachenko.jlv.Log;
import com.rdiachenko.jlv.SocketLogServer;
import com.rdiachenko.jlv.plugin.JlvConstants;
import com.rdiachenko.jlv.plugin.Operation;
import com.rdiachenko.jlv.plugin.PreferenceStoreUtils;

public class LogListViewController {

  private SocketLogServer server;
  private LogCollector logCollector;
  private ViewRefresher viewRefresher;

  public void initLogCollector(CircularBuffer<Log> input) {
    if (server != null && logCollector != null) {
      server.removeLogEventListener(logCollector);
    }
    logCollector = new LogCollector(input);

    if (server != null) {
      server.addLogEventListener(logCollector);
    }
  }

  public void initViewRefresher(Operation callback) {
    viewRefresher = new ViewRefresher(callback);
  }

  public void dispose() {
    stopServer();
    stopViewRefresher();
  }

  public void startServer() {
    Preconditions.checkNotNull(logCollector, "Log collector is null");
    server = new SocketLogServer(PreferenceStoreUtils.getInt(JlvConstants.SERVER_PORT_PREF_KEY));
    server.addLogEventListener(logCollector);
    server.start();
  }

  public void stopServer() {
    if (server != null) {
      server.stop();
    }
  }

  public void startViewRefresher() {
    Preconditions.checkNotNull(viewRefresher, "View refresher is null");
    viewRefresher.start();
  }

  public void stopViewRefresher() {
    if (viewRefresher != null) {
      viewRefresher.stop();
    }
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
