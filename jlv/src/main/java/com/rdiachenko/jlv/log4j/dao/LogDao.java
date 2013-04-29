package com.rdiachenko.jlv.log4j.dao;

import com.rdiachenko.jlv.log4j.domain.Log;
import com.rdiachenko.jlv.log4j.domain.LogContainer;

public interface LogDao {

	public void initDb();

	public LogContainer getTailingLogs(int tail);

	public void insert(Log log);
}
