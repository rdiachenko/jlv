package com.github.rd.jlv.eclipse.ui.views;

import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rd.jlv.CircularBuffer;
import com.github.rd.jlv.Log;
import com.github.rd.jlv.server.Server;
import com.google.common.eventbus.Subscribe;

public class LoglistViewController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

//	private static final PreferenceManager preferenceManager = JlvActivator.getDefault().getPreferenceManager();

	private CircularBuffer<Log> logContainer;
	private final LogEventListener logEventListener;
	private final Timer viewUpdater;

	private Server server;

	public LoglistViewController(LoglistView view) {
//		logContainer = new LogCollection(preferenceManager.getLogsBufferSize());
		logEventListener = new LogEventListener();
		viewUpdater = new Timer(view);
		viewUpdater.start();
	}

	public CircularBuffer<Log> getLogContainer() {
		return logContainer;
	}

	public void startServer() {
//		server = new Server(preferenceManager.getServerPortNumber());
		server.addLogEventListener(logEventListener);
		server.start();
	}

	public void stopServer() {
		if (server != null) {
			server.stop();
		}
	}

	public void dispose() {
		server.removeLogEventListener(logEventListener);
		viewUpdater.stopTimer();
		stopServer();
	}

	public void clearLogContainer() {
		logContainer.clear();
	}

	private static class Timer extends Thread {

		private final Logger logger = LoggerFactory.getLogger(getClass());

		private volatile boolean running = true;

		private LoglistView view;

		public Timer(LoglistView view) {
			this.view = view;
			setDaemon(true);
		}

		@Override
		public void run() {
			logger.debug("Log list view updater was run");

			while (running) {
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						view.refreshViewer();
					}
				});
//				try {
//					sleep(preferenceManager.getRefreshingTime());
//				} catch (InterruptedException e) {
//					Thread.currentThread().interrupt();
//					logger.error("Timer thread was interrupted:", e);
//				}
			}
			logger.debug("Log list view updater was stopped");
		}

		public void stopTimer() {
			running = false;
			logger.debug("Stopping log list view updater...");
		}
	}
	
	private class LogEventListener {
		
		@Subscribe
		public void handle(Log log) {
			logContainer.add(log);
		}
	}
}
