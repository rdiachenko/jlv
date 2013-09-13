package com.rdiachenko.jlv.ui.views;

import java.io.IOException;

import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rdiachenko.jlv.log4j.dao.DaoProvider;
import com.rdiachenko.jlv.log4j.dao.LogDao;
import com.rdiachenko.jlv.log4j.domain.Log;
import com.rdiachenko.jlv.log4j.domain.LogContainer;
import com.rdiachenko.jlv.log4j.domain.LogEventContainer;
import com.rdiachenko.jlv.log4j.domain.LogEventListener;
import com.rdiachenko.jlv.log4j.socketappender.Server;
import com.rdiachenko.jlv.ui.preferences.PreferenceManager;

public class LogListViewController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final LogContainer logContainer;

	private final LogEventListener logEventListener;

	private final Timer viewUpdater;

	private final LogDao logDao;

	private Server server;

	public LogListViewController(LogListView view) {
		logDao = DaoProvider.LOG_DAO.getLogDao();
		logDao.initDb();
		logContainer = new LogContainer(PreferenceManager.getLogListViewBufferSize());

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

	public LogContainer getLogContainer() {
		return logContainer;
	}

	public void startServer() {
		try {
			server = new Server(PreferenceManager.getServerPortNumber());
			server.start();
		} catch (IOException e) {
			logger.error("IOException occurred while starting server:", e);
		}
	}

	public void stopServer() {
		if (server != null) {
			server.stopServer();
			server = null;
		}
	}

	public void dispose() {
		LogEventContainer.removeListener(logEventListener);
		viewUpdater.stopTimer();
		stopServer();
		logDao.dropDb();
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
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						view.refreshViewer();
					}
				});
				try {
					sleep(PreferenceManager.getLogListViewRefreshingTime());
				} catch (InterruptedException e) {
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
