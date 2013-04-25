package com.rdiachenko.jlv.model;

import com.rdiachenko.jlv.log4j.domain.Log;

public enum LogField {

    LEVEL("Level"),
    CATEGORY("Category"),
    MESSAGE("Message"),
    LINE("Line"),
    DATE("Date");

    private String name;

    private LogField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

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
        default:
            throw new IllegalStateException("Only [LEVEL,CATEGORY,MESSAGE,LINE,DATE] log fields are available.");
        }
        return value;
    }
}
