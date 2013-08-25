package com.rdiachenko.jlv.log4j;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;

import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;
import org.junit.Assert;
import org.junit.Test;

import com.rdiachenko.jlv.log4j.domain.Log;

public class LogConverterTest {

	@Test(expected = InvocationTargetException.class)
	public void illegalInstanceCreation() throws NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Constructor<LogConverter> instance = LogConverter.class.getDeclaredConstructor();
		instance.setAccessible(true);
		instance.newInstance();
	}

	@Test
	public void testFullLoggingEventConvertion() {
		LocationInfo mockLocationInfo = mock(LocationInfo.class);
		when(mockLocationInfo.getClassName()).thenReturn("Class name");
		when(mockLocationInfo.getFileName()).thenReturn("File name");
		when(mockLocationInfo.getLineNumber()).thenReturn("Line number");
		when(mockLocationInfo.getMethodName()).thenReturn("Method name");
		mockLocationInfo.fullInfo = "Full location info";

		ThrowableInformation mockThrowableInfo = mock(ThrowableInformation.class);
		when(mockThrowableInfo.getThrowableStrRep()).thenReturn(new String[] { "Exception1", "Exception2" });

		LoggingEvent mockLoggingEvent = mock(LoggingEvent.class);
		when(mockLoggingEvent.getLoggerName()).thenReturn("Logger name");
		when(mockLoggingEvent.getThreadName()).thenReturn("Thread name");
		when(mockLoggingEvent.getRenderedMessage()).thenReturn("Rendered message");
		when(mockLoggingEvent.getLevel()).thenReturn(Level.INFO);
		when(mockLoggingEvent.getLocationInformation()).thenReturn(mockLocationInfo);
		when(mockLoggingEvent.getThrowableInformation()).thenReturn(mockThrowableInfo);

		Log log = LogConverter.convert(mockLoggingEvent);
		Assert.assertEquals("Logger name", log.getCategoryName());
		Assert.assertEquals("Thread name", log.getThreadName());
		Assert.assertEquals("Rendered message", log.getMessage());
		Assert.assertEquals("INFO", log.getLevel());
		Assert.assertEquals("Class name", log.getClassName());
		Assert.assertEquals("File name", log.getFileName());
		Assert.assertEquals("Line number", log.getLineNumber());
		Assert.assertEquals("Method name", log.getMethodName());
		Assert.assertEquals("Full location info", log.getLocationInfo());
		Assert.assertEquals("Exception1\nException2\n", log.getThrowable());
		Assert.assertEquals("", log.getMs());
		Assert.assertEquals("", log.getDate());
	}

	@Test
	public void testEmptyLoggingEvent() {
		LoggingEvent mockLoggingEvent = mock(LoggingEvent.class);
		when(mockLoggingEvent.getLevel()).thenReturn(Level.ERROR);
		when(mockLoggingEvent.getLocationInformation()).thenReturn(null);
		when(mockLoggingEvent.getThrowableInformation()).thenReturn(null);
		Log log = LogConverter.convert(mockLoggingEvent);
		Assert.assertEquals("", log.getCategoryName());
		Assert.assertEquals("", log.getThreadName());
		Assert.assertEquals("", log.getMessage());
		Assert.assertEquals("ERROR", log.getLevel());
		Assert.assertEquals("", log.getClassName());
		Assert.assertEquals("", log.getFileName());
		Assert.assertEquals("", log.getLineNumber());
		Assert.assertEquals("", log.getMethodName());
		Assert.assertEquals("", log.getLocationInfo());
		Assert.assertEquals("", log.getThrowable());
		Assert.assertEquals("", log.getMs());
		Assert.assertEquals("", log.getDate());
	}

	@Test
	public void testEmptyThrowableInfo() {
		ThrowableInformation mockThrowableInfo = mock(ThrowableInformation.class);
		when(mockThrowableInfo.getThrowableStrRep()).thenReturn(null);

		LoggingEvent mockLoggingEvent = mock(LoggingEvent.class);
		when(mockLoggingEvent.getLevel()).thenReturn(Level.ALL);
		when(mockLoggingEvent.getThrowableInformation()).thenReturn(mockThrowableInfo);

		Log log = LogConverter.convert(mockLoggingEvent);
		Assert.assertEquals("", log.getThrowable());
		Assert.assertEquals("ALL", log.getLevel());
	}

	@Test
	public void testFullTimestamp() {
		Category mockCategory = mock(Category.class);
		when(mockCategory.getName()).thenReturn(null);
		LoggingEvent mockLoggingEvent = new LoggingEvent(null, mockCategory, 1234567L, Level.FATAL, null, null);
		Log log = LogConverter.convert(mockLoggingEvent);

		Assert.assertEquals("FATAL", log.getLevel());
		Assert.assertEquals("1234567", log.getMs());

		String expectedDate = new Timestamp(1234567L).toString();
		Assert.assertEquals(expectedDate, log.getDate());
	}
}
