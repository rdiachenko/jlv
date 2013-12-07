package com.github.rd.jlv.log4j;

import com.github.rd.jlv.log4j.domain.Log;

public enum LogField {

	LEVEL,
	CATEGORY,
	MESSAGE,
	LINE,
	DATE,
	THROWABLE;

	public String getValue(Log log) {
		String value = "";

		switch (this) {
		case LEVEL:
			value = log.getLevel();
			break;
		case CATEGORY:
			value = log.getCategoryName();
			break;
		case MESSAGE:
			value = log.getMessage();
			break;
		case LINE:
			value = log.getLineNumber();
			break;
		case DATE:
			value = log.getDate();
			break;
		case THROWABLE:
			value = log.getThrowable();
			break;
		default:
			throw new IllegalStateException(
					"Only [LEVEL,CATEGORY,MESSAGE,LINE,DATE,THROWABLE] log fields are available. "
							+ "Current value: " + this);
		}
		return value;
	}
}
