package com.rdiachenko.jlv.plugin;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Preconditions;

public enum LogLevel {

    DEBUG, INFO, WARN, ERROR, FATAL, OTHER;

    private static final Map<String, LogLevel> STR_TO_LEVEL = new HashMap<>();

    static {
        for (LogLevel level : values()) {
            STR_TO_LEVEL.put(level.name(), level);
        }
    }
    
    public static LogLevel toLogLevel(String value) {
        Preconditions.checkNotNull(value, "Log level string value is null");
        LogLevel level = STR_TO_LEVEL.get(value.toUpperCase());

        if (level == null) {
            level = OTHER;
        }
        return level;
    }
}
