package com.github.rd.jlv.log4j.domain;

import org.junit.Assert;
import org.junit.Test;

public class LogCollectionTest {

	@Test(expected = IllegalArgumentException.class)
	public void testWrongCapacity() {
		new LogCollection(0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeIndex() {
		LogCollection logs = new LogCollection(5);
		Log log1 = new Log.Builder().message("log1").build();
		logs.add(log1);
		logs.get(-7);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIndexOutOfBounds() {
		LogCollection logs = new LogCollection(5);
		Log log1 = new Log.Builder().message("log1").build();
		logs.add(log1);
		logs.get(7);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIndexOnZiroSizeArray() {
		LogCollection logs = new LogCollection(5);
		logs.get(0);
	}

	@Test
	public void testCollectionOverflow() {
		Log log1 = new Log.Builder().message("log1").build();
		Log log2 = new Log.Builder().message("log2").build();
		Log log3 = new Log.Builder().message("log3").build();
		Log log4 = new Log.Builder().message("log4").build();

		LogCollection logs = new LogCollection(2);
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
	public void testSingleElementCollection() {
		Log log1 = new Log.Builder().message("log1").build();
		Log log2 = new Log.Builder().message("log2").build();
		LogCollection logs = new LogCollection(1);
		logs.add(log1);
		logs.add(log2);

		Assert.assertTrue(logs.size() == 1);
		Assert.assertEquals(log2, logs.get(0));
		Assert.assertArrayEquals(new Log[] { log2 }, logs.toArray());
	}

	@Test
	public void testLogContainer() {
		Log log1 = new Log.Builder().message("log1").build();
		Log log2 = new Log.Builder().message("log2").build();
		Log log3 = new Log.Builder().message("log3").build();
		Log log4 = new Log.Builder().message("log4").build();
		Log log5 = new Log.Builder().message("log5").build();
		Log log6 = new Log.Builder().message("log6").build();

		LogCollection logs = new LogCollection(5);
		logs.add(log1);
		logs.add(log2);
		logs.add(log3);
		logs.add(log4);
		logs.add(log5);
		logs.add(log6);

		Assert.assertTrue(logs.size() == 5);
		Assert.assertEquals(log2, logs.get(0));
		Assert.assertEquals(log3, logs.get(1));
		Assert.assertEquals(log4, logs.get(2));
		Assert.assertEquals(log5, logs.get(3));
		Assert.assertEquals(log6, logs.get(4));
		Assert.assertArrayEquals(new Log[] { log2, log3, log4, log5, log6 }, logs.toArray());

		logs.clear();
		Assert.assertTrue(logs.size() == 0);
	}
}
