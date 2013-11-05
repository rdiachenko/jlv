package com.github.incode.jlv.log4j.dao;

import com.github.incode.jlv.log4j.domain.Log;
import com.github.incode.jlv.log4j.domain.LogContainer;

public interface LogDao {

	public void initDb();

	public void dropDb();

	public LogContainer getTailingLogs(int tail);

	public void insert(Log log);
}
