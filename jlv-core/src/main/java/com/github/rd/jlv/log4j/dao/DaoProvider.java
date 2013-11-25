package com.github.rd.jlv.log4j.dao;

public enum DaoProvider {

	LOG_DAO(new LogDaoImpl());

	private LogDao logDao;

	private DaoProvider(LogDao logDao) {
		this.logDao = logDao;
	}

	public LogDao getLogDao() {
		return logDao;
	}
}
