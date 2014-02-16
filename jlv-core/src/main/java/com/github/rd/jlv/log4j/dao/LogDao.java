package com.github.rd.jlv.log4j.dao;

import com.github.rd.jlv.log4j.domain.Log;

public interface LogDao {

	public void initDb();

	public void dropDb();

	public Log[] getTailingLogs(int tail);

	public void insert(Log log);
}
