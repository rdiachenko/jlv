package com.github.rd.jlv;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;

public class CircularBufferTest {

	private static final int DEFAULT_BUFFER_SIZE = 5;
	
	@Test(expected = IllegalArgumentException.class)
	public void testWrongCapacity() {
		new CircularBuffer<Log>(0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeIndex() {
		CircularBuffer<Log> logs = new CircularBuffer<>(DEFAULT_BUFFER_SIZE);
		logs.get(-7);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIndexOutOfBounds() {
		CircularBuffer<Log> logs = new CircularBuffer<>(DEFAULT_BUFFER_SIZE);
		logs.get(7);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRetrivalOnEmptyBuffer() {
		CircularBuffer<Log> logs = new CircularBuffer<>(DEFAULT_BUFFER_SIZE);
		logs.get(0);
	}

	@Test
	public void testBufferOverflow() {
		Log log1 = createLogFromMessage("log1");
		Log log2 = createLogFromMessage("log2");
		Log log3 = createLogFromMessage("log3");
		Log log4 = createLogFromMessage("log4");

		CircularBuffer<Log> logs = new CircularBuffer<>(2);
		logs.add(log1);
		logs.add(log2);
		logs.add(log3);
		logs.add(log4);

		Assert.assertTrue(logs.size() == 2);
		Assert.assertEquals(log3, logs.get(0));
		Assert.assertEquals(log4, logs.get(1));
		Assert.assertArrayEquals(new Log[] { log3, log4 }, logs.toArray());

		logs.clear();
		Assert.assertTrue(logs.size() == 0);
	}
	
	@Test
	public void testBufferIterator() {
		Log log = createLogFromMessage("log1");
		CircularBuffer<Log> logs = new CircularBuffer<>(1);
		logs.add(log);
		Iterator<Log> iter = logs.iterator();
		
		Assert.assertTrue(iter.hasNext());
		Assert.assertEquals(log, iter.next());
		Assert.assertFalse(iter.hasNext());
	}
	
	private static Log createLogFromMessage(String message) {
		return new Log.Builder().message(message).build();
	}
}
