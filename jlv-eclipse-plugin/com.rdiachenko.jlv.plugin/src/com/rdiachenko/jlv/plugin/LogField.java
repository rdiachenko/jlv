package com.rdiachenko.jlv.plugin;

import com.rdiachenko.jlv.Log;

public enum LogField {
    
    DATE("Date"),
    LEVEL("Level"),
    THREAD("Thread"),
    LOGGER("Logger"),
    MESSAGE("Message"),
    CLASS("Class"),
    METHOD("Method"),
    LINE("Line"),
    FILE("File"),
    MARKER("Marker"),
    THROWABLE("Throwable"),
    NDC("Ndc"),
    MDC("Mdc");
    
    private final String name;
    
    private LogField(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public String valueOf(Log log) {
        switch (this) {
        case DATE:
            return log.getDate();
        case LEVEL:
            return log.getLevel();
        case THREAD:
            return log.getThreadName();
        case LOGGER:
            return log.getLoggerName();
        case MESSAGE:
            return log.getMessage();
        case CLASS:
            return log.getClassName();
        case METHOD:
            return log.getMethodName();
        case LINE:
            return log.getLineNumber();
        case FILE:
            return log.getFileName();
        case MARKER:
            return log.getMarker();
        case THROWABLE:
            return log.getThrowable();
        case NDC:
            return log.getNdc();
        case MDC:
            return log.getMdc();
        default:
            throw new IllegalArgumentException("No value for " + name());
        }
    }
}
