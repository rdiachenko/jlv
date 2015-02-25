package com.github.rd.jlv.handler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.rd.jlv.Log;

public class Log4j1ConverterTest {

	private static String categoryClass;
	private static Category category;
	private static long timestamp;
	private static Level level;
	private static String message;
	private static String threadName;
	private static ThrowableInformation throwable;
	private static String ndc;
	private static LocationInfo localInfo;
	private static Map<String, String> mdc;
	
	private LogConverter converter = new Log4j1Converter();
	
	@BeforeClass
	public static void init() {
		categoryClass = Log4j1ConverterTest.class.getSimpleName();
		category = mock(Category.class);
		timestamp = 123456789L;
		level = Level.INFO;
		message = "ping";
		threadName = "test-thread";
		throwable = new ThrowableInformation(new IllegalArgumentException("wrong parameter format"), category);
		ndc = "test-ndc";
		
		localInfo = mock(LocationInfo.class);
		when(localInfo.getClassName()).thenReturn(Log4j1ConverterTest.class.getName());
		when(localInfo.getFileName()).thenReturn("Log4j1ConverterTest");
		when(localInfo.getLineNumber()).thenReturn("7");
		when(localInfo.getMethodName()).thenReturn("createFullLog");
		localInfo.fullInfo = localInfo.getClassName() 
				+ "." + localInfo.getMethodName() 
				+ "(" + localInfo.getFileName() 
				+ ".java:" 
				+ localInfo.getLineNumber() 
				+ ")";
		
		mdc = new HashMap<>();
		mdc.put("client_id", "5");
		mdc.put("session_id", "a7b");
	}
	
	@Test
	public void testConvertFullLog() {
		Log actual = converter.convert(createFullLog());
		Assert.assertTrue(actual.getCategoryName().isEmpty());
		Assert.assertEquals(String.valueOf(timestamp), actual.getMs());
		Assert.assertEquals(level.toString(), actual.getLevel());
		Assert.assertEquals(message, actual.getMessage());
		Assert.assertEquals(threadName, actual.getThreadName());
		Assert.assertEquals(throwableToString(throwable), actual.getThrowable());
		Assert.assertEquals(ndc, actual.getNdc());
		Assert.assertEquals(localInfo.fullInfo, actual.getLocationInfo());
		Assert.assertEquals(localInfo.getClassName(), actual.getClassName());
		Assert.assertEquals(localInfo.getFileName(), actual.getFileName());
		Assert.assertEquals(localInfo.getLineNumber(), actual.getLineNumber());
		Assert.assertEquals(localInfo.getMethodName(), actual.getMethodName());
		Assert.assertEquals(mdc.toString(), actual.getMdc());
	}
	
	@Test
	public void testConvertPartialLog() {
		Log actual = converter.convert(createPartialLog());
		Assert.assertTrue(actual.getCategoryName().isEmpty());
		Assert.assertTrue(actual.getMs().isEmpty());
		Assert.assertEquals(level.toString(), actual.getLevel());
		Assert.assertEquals(message, actual.getMessage());
		Assert.assertEquals(threadName, actual.getThreadName());
		Assert.assertTrue(actual.getThrowable().isEmpty());
		Assert.assertTrue(actual.getNdc().isEmpty());
		Assert.assertTrue(actual.getLocationInfo().isEmpty());
		Assert.assertTrue(actual.getClassName().isEmpty());
		Assert.assertTrue(actual.getFileName().isEmpty());
		Assert.assertTrue(actual.getLineNumber().isEmpty());
		Assert.assertTrue(actual.getMethodName().isEmpty());
		Assert.assertTrue(actual.getMdc().isEmpty());
	}
	
	private static LoggingEvent createFullLog() {
		LoggingEvent log = new LoggingEvent(
				categoryClass,
				category,
				timestamp,
				level,
				message,
				threadName,
				throwable,
				ndc,
				localInfo,
				mdc);
		return log;
	}
	
	private static LoggingEvent createPartialLog() {
		LoggingEvent log = new LoggingEvent(
				categoryClass,
				category,
				-1,
				level,
				message,
				threadName,
				null,
				null,
				null,
				null);
		return log;
	}
	
	private static String throwableToString(ThrowableInformation throwable) {
		StringBuilder res = new StringBuilder();
		String lineSeparator = "\n";

		for (String exception : throwable.getThrowableStrRep()) {
			res.append(exception).append(lineSeparator);
		}
		return res.toString();
	}
}
