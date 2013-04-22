package com.rdiachenko.jlv.log4j.dao;

import java.sql.SQLException;

import com.rdiachenko.jlv.log4j.domain.LogContainer;

public interface LogDao {

	public void dropAndCreateLogsTable() throws SQLException;

	public LogContainer getTailingLogs(int tail);

}
