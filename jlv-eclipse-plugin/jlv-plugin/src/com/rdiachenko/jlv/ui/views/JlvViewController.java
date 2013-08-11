package com.rdiachenko.jlv.ui.views;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rdiachenko.jlv.log4j.dao.LogDao;
import com.rdiachenko.jlv.log4j.dao.LogDaoImpl;
import com.rdiachenko.jlv.log4j.domain.Log;
import com.rdiachenko.jlv.log4j.domain.LogContainer;
import com.rdiachenko.jlv.log4j.domain.LogEventContainer;
import com.rdiachenko.jlv.log4j.domain.LogEventListener;
import com.rdiachenko.jlv.log4j.socketappender.Server;
import com.rdiachenko.jlv.ui.preferences.PreferenceManager;

public class JlvViewController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final JlvView view;

	private final LogContainer logContainer;

	private final LogEventListener logEventListener;

	private final LogDao logDao;

	private Server server;

	private ExecutorService executor;

	public JlvViewController(JlvView view) {
		this.view = view;
		logDao = new LogDaoImpl();
		logDao.initDb();
		logContainer = new LogContainer(PreferenceManager.getLogViewBufferSize());

		logEventListener = new LogEventListener() {
			private long startTime = Calendar.getInstance().getTimeInMillis();

			@Override
			public void handleLogEvent(final Log log) {
				logContainer.add(log);
				long updateTime = Calendar.getInstance().getTimeInMillis() - startTime;

				if (updateTime >= PreferenceManager.getLogViewRefreshingTime()) {
					refreshViewer();
					startTime = Calendar.getInstance().getTimeInMillis();
				}
			}

			@Override
			public void dispose() {
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
			executor = Executors.newSingleThreadExecutor();
			executor.execute(new Runnable() {
				@Override
				public void run() {
					logger.debug("Starting server from Jlv view...");
					server.start();
					logger.debug("Start is completed");
				}
			});
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	public void stopServer() {
		try {
			logger.debug("Stopping server from Jlv view...");

			if (server != null && executor != null) {
				server.stop();
				executor.shutdown();

				if (!executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)) {
					executor.shutdownNow();
				}
				logger.debug("Stop is completed");
			}
		} catch (InterruptedException e) {
			logger.error("", e);
		} finally {
			server = null;
			executor = null;
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
