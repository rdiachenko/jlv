package com.rdiachenko.jlv.log4j.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.h2.tools.TriggerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rdiachenko.jlv.log4j.domain.Log;
import com.rdiachenko.jlv.log4j.domain.LogEventContainer;
import com.rdiachenko.jlv.log4j.domain.LogFieldName;

public class LogsInsTrigger extends TriggerAdapter {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void fire(Connection conn, ResultSet oldRow, ResultSet newRow) throws SQLException {
		if (newRow.next()) {
			Log log = (new Log.Builder()).categoryName(newRow.getString(LogFieldName.CATEGORY.getName()))
					.className(newRow.getString(LogFieldName.CLASS.getName()))
					.date(newRow.getString(LogFieldName.DATE.getName()))
					.fileName(newRow.getString(LogFieldName.FILE.getName()))
					.locationInfo(newRow.getString(LogFieldName.LOCATION_INFO.getName()))
					.lineNumber(newRow.getString(LogFieldName.LINE.getName()))
					.methodName(newRow.getString(LogFieldName.METHOD.getName()))
					.level(newRow.getString(LogFieldName.LEVEL.getName()))
					.ms(newRow.getString(LogFieldName.MILLISECONDS.getName()))
					.threadName(newRow.getString(LogFieldName.THREAD.getName()))
					.message(newRow.getString(LogFieldName.MESSAGE.getName()))
					.throwable(newRow.getString(LogFieldName.THROWABLE.getName()))
					.build();
			LogEventContainer.notifyListeners(log);
		}
	}

	@Override
	public void close() throws SQLException {
		LogEventContainer.disposeEvent();
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
