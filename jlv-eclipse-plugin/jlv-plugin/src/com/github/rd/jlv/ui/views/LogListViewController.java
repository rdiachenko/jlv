package com.github.rd.jlv.ui.views;

import java.io.IOException;

import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rd.jlv.JlvActivator;
import com.github.rd.jlv.log4j.domain.Log;
import com.github.rd.jlv.log4j.domain.LogCollection;
import com.github.rd.jlv.log4j.domain.LogEventContainer;
import com.github.rd.jlv.log4j.domain.LogEventListener;
import com.github.rd.jlv.log4j.socketappender.Server;

public class LogListViewController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final LogCollection logContainer;

	private final LogEventListener logEventListener;

	private final Timer viewUpdater;

	private Server server;

	public LogListViewController(LogListView view) {
		logContainer = new LogCollection(JlvActivator.getDefault().getPreferenceManager().getLogsBufferSize());

		logEventListener = new LogEventListener() {
			@Override
			public void handleLogEvent(final Log log) {
				logContainer.add(log);
			}
		};
		LogEventContainer.addListener(logEventListener);

		viewUpdater = new Timer(view);
		viewUpdater.start();
	}

	public LogCollection getLogContainer() {
		return logContainer;
	}

	public void startServer() {
		try {
			server = new Server(JlvActivator.getDefault().getPreferenceManager().getServerPortNumber());
			server.start();
		} catch (IOException e) {
			logger.error("IOException occurred while starting server:", e);
		}
	}

	public void stopServer() {
		if (server != null) {
			server.shutdown();
			server = null;
		}
	}

	public void dispose() {
		LogEventContainer.removeListener(logEventListener);
		viewUpdater.stopTimer();
		stopServer();
	}

	public void clearLogContainer() {
		logContainer.clear();
	}

	private static class Timer extends Thread {

		private final Logger logger = LoggerFactory.getLogger(getClass());

		private volatile boolean running = true;

		private LogListView view;

		public Timer(LogListView view) {
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
				try {
					sleep(JlvActivator.getDefault().getPreferenceManager().getLogsRefreshingTime());
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					logger.error("Timer thread was interrupted:", e);
				}
			}
			logger.debug("Log list view updater was stopped");
		}

		public void stopTimer() {
			running = false;
			logger.debug("Stopping log list view updater...");
		}
	}
}
