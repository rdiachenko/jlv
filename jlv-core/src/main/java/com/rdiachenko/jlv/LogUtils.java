package com.rdiachenko.jlv;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

import com.google.common.base.Strings;

public final class LogUtils {

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private LogUtils() {
        // Utility class
    }

    public static String nullToEmpty(String val) {
        return Strings.nullToEmpty(val);
    }

    public static String formatDate(long timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), TimeZone
                .getDefault().toZoneId());
        return dateTime.format(DATE_FORMAT);
    }
}
