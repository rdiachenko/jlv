package com.github.rd.jlv.handler;

import org.apache.log4j.spi.LoggingEvent;
import org.junit.Assert;
import org.junit.Test;

import com.github.rd.jlv.TestUtils;

public class LogTypeTest {

	@Test
	public void testTypeOfLog4j1() {
		LoggingEvent log4j1 = TestUtils.createLog4j1LogFromMessage("simple");
		LogType actualLogType = LogType.typeOf(log4j1);
		Assert.assertEquals(LogType.LOG4J1, actualLogType);
		Assert.assertTrue("wrong converter type", actualLogType.converter() instanceof Log4j1Converter);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTypeOfUnknown() {
		LogType.typeOf("value");
	}
}
