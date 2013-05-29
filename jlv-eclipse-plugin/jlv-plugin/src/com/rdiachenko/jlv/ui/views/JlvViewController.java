package com.rdiachenko.jlv.ui.views;

import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
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

	private static final int VIEWER_BUFFER_SIZE = 5000;

	private final LogContainer logContainer;

	private final LogEventListener logEventListener;

	private final LogDao logDao;

	private Server server;

	public JlvViewController(final JlvView view) {
		logDao = new LogDaoImpl();
		logDao.initDb();
		logContainer = new LogContainer(VIEWER_BUFFER_SIZE);
		logEventListener = new LogEventListener() {
			@Override
			public void handleLogEvent(Log log) {
				logContainer.add(log);
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						view.refreshViewer();
					}
				});
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
			Job job = new Job("JLV server is running") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					logger.debug("Starting server from Jlv view ...");
					server.start();
					return Status.OK_STATUS;
				}
			};
			job.schedule();
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	public void stopServer() {
		logger.debug("Stopping server from Jlv view ...");
		if (server != null) {
			Job job = new Job("Stopping JLV server") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					server.stop();
					return Status.OK_STATUS;
				}
			};
			job.schedule();
		}
	}

	public void dispose() {
		LogEventContainer.removeListener(logEventListener);
		stopServer();
	}

	public void clearLogContainer() {
		logContainer.clear();
	}
}
