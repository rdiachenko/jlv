package com.rdiachenko.jlv.plugin;

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
}
