package com.github.rd.jlv.log4j.dao;

import com.github.rd.jlv.log4j.domain.Log;
import com.github.rd.jlv.log4j.domain.LogContainer;

public interface LogDao {

	public void initDb();

	public void dropDb();

	public LogContainer getTailingLogs(int tail);

	public void insert(Log log);
}
