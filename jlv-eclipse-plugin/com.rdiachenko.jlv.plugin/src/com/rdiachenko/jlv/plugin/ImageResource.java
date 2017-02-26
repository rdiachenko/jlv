package com.rdiachenko.jlv.plugin;

public enum ImageResource {
    
    CHECKBOX_CHECKED_ICON("/icons/checkboxchecked.gif"),
    CHECKBOX_UNCHECKED_ICON("/icons/checkboxunchecked.gif"),
    DEBUG_LOG_LEVEL_ICON("/icons/debug.png"),
    INFO_LOG_LEVEL_ICON("/icons/info.png"),
    WARN_LOG_LEVEL_ICON("/icons/warn.png"),
    ERROR_LOG_LEVEL_ICON("/icons/error.png"),
    FATAL_LOG_LEVEL_ICON("/icons/fatal.png"),
    OTHER_LOG_LEVEL_ICON("/icons/other.png");
    
    private String path;
    
    private ImageResource(String path) {
        this.path = path;
    }
    
    public String path() {
        return path;
    }
}
