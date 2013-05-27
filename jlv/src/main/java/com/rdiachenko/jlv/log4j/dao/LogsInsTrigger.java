package com.rdiachenko.jlv.log4j.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.h2.tools.TriggerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rdiachenko.jlv.log4j.domain.Log;
import com.rdiachenko.jlv.log4j.domain.LogEventContainer;

public class LogsInsTrigger extends TriggerAdapter {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void fire(Connection conn, ResultSet oldRow, ResultSet newRow) throws SQLException {
		logger.info("Triggering new log...");
		if (newRow.next()) {
			Log log = (new Log.Builder()).categoryName(newRow.getString("category"))
					.className(newRow.getString("class"))
					.date(newRow.getString("date"))
					.fileName(newRow.getString("file"))
					.locationInfo(newRow.getString("locInfo"))
					.lineNumber(newRow.getString("line"))
					.methodName(newRow.getString("method"))
					.level(newRow.getString("level"))
					.ms(newRow.getString("ms"))
					.threadName(newRow.getString("thread"))
					.message(newRow.getString("message"))
					.build();
			LogEventContainer.notifyListeners(log);
			logger.debug("Trigger new log: {}", log);
		}
	}

	@Override
	public void close() throws SQLException {
		logger.info("Closing database...");
		super.close();
		logger.info("The database is closed");
	}

	@Override
	public void remove() throws SQLException {
		logger.info("Dropping {} trigger...", getClass());
		super.remove();
		logger.info("The trigger was dropped");
	}
}
