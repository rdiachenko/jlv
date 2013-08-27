package com.rdiachenko.jlv.ui.views;

import java.io.IOException;
import java.util.Calendar;

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

	private final LogListView view;

	private final LogContainer logContainer;

	private final LogEventListener logEventListener;

	private final LogDao logDao;

	private Server server;

	public LogListViewController(LogListView view) {
		this.view = view;
		logDao = DaoProvider.LOG_DAO.getLogDao();
		logDao.initDb();
		logContainer = new LogContainer(PreferenceManager.getLogListViewBufferSize());

		logEventListener = new LogEventListener() {
			private long startTime = Calendar.getInstance().getTimeInMillis();

			@Override
			public void handleLogEvent(final Log log) {
				logContainer.add(log);
				long updateTime = Calendar.getInstance().getTimeInMillis() - startTime;

				if (updateTime >= PreferenceManager.getLogListViewRefreshingTime()) {
					refreshViewer();
					startTime = Calendar.getInstance().getTimeInMillis();
				}
			}

			@Override
			public void lastLogEvent() {
				refreshViewer();
				startTime = Calendar.getInstance().getTimeInMillis();
			}
		};
		LogEventContainer.addListener(logEventListener);
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
		stopServer();
		logDao.dropDb();
	}

	public void clearLogContainer() {
		logContainer.clear();
	}

	private void refreshViewer() {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				view.refreshViewer();
			}
		});
	}
}
