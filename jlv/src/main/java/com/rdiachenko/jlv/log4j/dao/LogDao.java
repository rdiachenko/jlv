package com.rdiachenko.jlv.log4j.dao;

import com.rdiachenko.jlv.log4j.domain.LogContainer;

public interface LogDao {

	public LogContainer getAllLogs();

	public LogContainer getTailingLogs(int tail);

}
