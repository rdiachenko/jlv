package com.rdiachenko.jlv.plugin;

public enum LogLevel {

    DEBUG, INFO, WARN, ERROR, FATAL, OTHER;

    public static LogLevel toLogLevel(String value) {
        for (LogLevel level : values()) {
            if (level.name().equalsIgnoreCase(value)) {
                return level;
            }
        }
        return LogLevel.OTHER;
    }
}
