package com.rdiachenko.jlv.log4j.domain;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;

public class LogContainerTest {

	@Test(expected = IllegalArgumentException.class)
	public void testWrongBufferSize() {
		new LogContainer(0);
	}

	@Test
	public void testLogContainer() {
		Log log1 = new Log.Builder().message("log1").build();
		Log log2 = new Log.Builder().message("log2").build();
		Log log3 = new Log.Builder().message("log3").build();
		Log log4 = new Log.Builder().message("log4").build();
		Log log5 = new Log.Builder().message("log5").build();
		Log log6 = new Log.Builder().message("log6").build();

		LogContainer logs = new LogContainer(5);
		logs.add(log1);
		logs.add(log2);
		logs.add(log3);
		logs.add(log4);
		logs.add(log5);
		logs.add(log6);

		Assert.assertTrue(logs.size() == 5);
		Assert.assertEquals(log2, logs.get());

		Iterator<Log> iter = logs.iterator();
		Log actualLog2 = iter.next();
		Log actualLog3 = iter.next();
		Log actualLog4 = iter.next();
		Log actualLog5 = iter.next();
		Log actualLog6 = iter.next();

		Assert.assertEquals(log2, actualLog2);
		Assert.assertEquals(log3, actualLog3);
		Assert.assertEquals(log4, actualLog4);
		Assert.assertEquals(log5, actualLog5);
		Assert.assertEquals(log6, actualLog6);

		logs.clear();
		Assert.assertTrue(logs.size() == 0);
	}
}
