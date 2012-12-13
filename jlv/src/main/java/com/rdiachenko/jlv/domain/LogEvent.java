package com.rdiachenko.jlv.domain;

public interface LogEvent {

	String getCategoryName();

    String getClassName();

    String getDate();

    String getFileName();

    String getLineNumber();

    String getMessage();

    String getMethodName();

    String getLevel();

}
