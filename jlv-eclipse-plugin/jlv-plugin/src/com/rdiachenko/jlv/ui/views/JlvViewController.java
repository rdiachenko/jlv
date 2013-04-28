package com.rdiachenko.jlv.ui.views;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rdiachenko.jlv.log4j.dao.LogDao;
import com.rdiachenko.jlv.log4j.dao.LogDaoImpl;
import com.rdiachenko.jlv.log4j.domain.Log;
import com.rdiachenko.jlv.log4j.domain.LogContainer;
import com.rdiachenko.jlv.log4j.socketappender.Server;
import com.rdiachenko.jlv.ui.preferences.PreferenceManager;

public class JlvViewController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private Server server;

	private LogDao logDao;

	public JlvViewController() {
		logDao = new LogDaoImpl();
		try {
			logDao.dropAndCreateLogsTable();
		} catch (SQLException e) {
			logger.error("", e);
		}
	}

	public void startServer() {
		try {
			server = new Server(PreferenceManager.getServerPortNumber());
			Job job = new Job("Server start job") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try {
						logger.debug("Starting server from Jlv view ...");
						server.start();
					} catch (IOException e) {
						logger.error("", e);
					} finally {
						stopServer();
					}
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
			Job job = new Job("Server stop job") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try {
						server.stop();
					} catch (IOException e) {
						logger.error("", e);
					}
					return Status.OK_STATUS;
				}
			};
			job.schedule();
		}
	}

	public void dispose() {
		stopServer();
	}

	public Log[] getLogs() {
		LogContainer logContainer = logDao.getTailingLogs(5000);
		Iterator<Log> iterator = logContainer.iterator();
		Log[] logs = new Log[logContainer.size()];

		for (int i = 0; iterator.hasNext(); i++) {
			logs[i] = iterator.next();
		}
		return logs;
	}
}
