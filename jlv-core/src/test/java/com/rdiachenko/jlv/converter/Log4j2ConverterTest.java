package com.rdiachenko.jlv.converter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.ThreadContext.ContextStack;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.message.FormattedMessage;
import org.apache.logging.log4j.util.StringMap;
import org.junit.Assert;
import org.junit.Test;

import com.rdiachenko.jlv.Log;
import com.rdiachenko.jlv.LogUtils;

public class Log4j2ConverterTest {
    
    private static final long NOW = System.currentTimeMillis();
    private static final String LEVEL = "INFO";
    private static final String THREAD_NAME = "main";
    private static final String LOGGER_NAME = Log4j2ConverterTest.class.getName();
    private static final String MESSAGE = "simple message";
    private static final String CLASS_NAME = Log4j2ConverterTest.class.getName();
    private static final String METHOD_NAME = "test";
    private static final String LINE_NUMBER = "7";
    private static final String FILE_NAME = Log4j2ConverterTest.class.getSimpleName() + ".java";
    private static final String MARKER = "TEST";
    
    private static final String THROWABLE = "java.lang.IllegalStateException: test exception"
            + System.lineSeparator() + "\tat com.rdiachenko.jlv.Main.main(Main.java:12)";
    
    private static final String NDC = "ndc-value";
    
    private static final Map<String, String> MDC_MAP = new HashMap<>();
    static {
        MDC_MAP.put("mdc-key", "mdc-value");
    }
    
    @Test
    public void testConverter() {
        StringMap mockMdc = mock(StringMap.class);
        when(mockMdc.toMap()).thenReturn(MDC_MAP);
        
        ContextStack mockNdc = mock(ContextStack.class);
        when(mockNdc.toString()).thenReturn(NDC);
        
        ThrowableProxy mockThrowableProxy = mock(ThrowableProxy.class);
        when(mockThrowableProxy.getExtendedStackTraceAsString()).thenReturn(THROWABLE);
        
        LogEvent logEvent = Log4jLogEvent.newBuilder()
                .setTimeMillis(NOW)
                .setLevel(org.apache.logging.log4j.Level.toLevel(LEVEL))
                .setThreadName(THREAD_NAME)
                .setLoggerName(LOGGER_NAME)
                .setMessage(new FormattedMessage(MESSAGE))
                .setSource(new StackTraceElement(CLASS_NAME, METHOD_NAME, FILE_NAME, Integer.parseInt(LINE_NUMBER)))
                .setMarker(new MarkerManager.Log4jMarker(MARKER))
                .setThrownProxy(mockThrowableProxy)
                .setContextStack(mockNdc)
                .setContextData(mockMdc)
                .build();
        
        Log log = LogConverterType.LOG4J_2.convert(logEvent);
        Assert.assertEquals(LogUtils.formatDate(NOW), log.getDate());
        Assert.assertEquals(LEVEL, log.getLevel());
        Assert.assertEquals(THREAD_NAME, log.getThreadName());
        Assert.assertEquals(LOGGER_NAME, log.getLoggerName());
        Assert.assertEquals(MESSAGE, log.getMessage());
        Assert.assertEquals(CLASS_NAME, log.getClassName());
        Assert.assertEquals(METHOD_NAME, log.getMethodName());
        Assert.assertEquals(LINE_NUMBER, log.getLineNumber());
        Assert.assertEquals(FILE_NAME, log.getFileName());
        Assert.assertEquals(MARKER, log.getMarker());
        Assert.assertEquals(THROWABLE, log.getThrowable());
        Assert.assertEquals(NDC, log.getNdc());
        Assert.assertEquals(MDC_MAP.toString(), log.getMdc());
    }
}
