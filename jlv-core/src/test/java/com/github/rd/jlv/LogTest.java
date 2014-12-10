package com.github.rd.jlv;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.github.rd.jlv.Log;

public class LogTest {

	@Test
	public void testLogsEquality() {
		Log logOne = (new Log.Builder()).categoryName("category")
				.className("class")
				.date("date")
				.fileName("file")
				.locationInfo("locInfo")
				.lineNumber("line")
				.methodName("method")
				.level("level")
				.ms("ms")
				.threadName("thread")
				.message("message")
				.throwable("exception")
				.build();

		Log logTwo = (new Log.Builder()).categoryName("category")
				.className("class")
				.date("date")
				.fileName("file")
				.locationInfo("locInfo")
				.lineNumber("line")
				.methodName("method")
				.level("level")
				.ms("ms")
				.threadName("thread")
				.message("message")
				.throwable("exception")
				.build();

		Set<Log> set = new HashSet<Log>();
		set.add(logOne);
		set.add(logTwo);
		set.add(new Log.Builder().build());

		Assert.assertEquals(2, set.size());
		Assert.assertTrue(set.contains(logOne));
		Assert.assertTrue(set.contains(logTwo));
		Assert.assertTrue(set.contains(new Log.Builder().build()));

		Assert.assertTrue(logOne.equals(logOne));
		Assert.assertFalse(logOne.equals(null));
		Assert.assertFalse(logOne.equals("ololo"));
		Assert.assertTrue(logOne.equals(logTwo));

		Assert.assertFalse(logOne.equals(new Log.Builder().build()));
		Assert.assertFalse(logOne.equals(new Log.Builder().categoryName("category").build()));
		Assert.assertFalse(logOne.equals(new Log.Builder().categoryName("category").className("class").build()));
		Assert.assertFalse(logOne.equals(new Log.Builder().categoryName("category").className("class").date("date")
				.build()));
		Assert.assertFalse(logOne.equals(new Log.Builder().categoryName("category").className("class").date("date")
				.fileName("file")
				.build()));
		Assert.assertFalse(logOne.equals(new Log.Builder().categoryName("category").className("class").date("date")
				.fileName("file")
				.locationInfo("locInfo")
				.build()));
		Assert.assertFalse(logOne.equals(new Log.Builder().categoryName("category").className("class").date("date")
				.fileName("file")
				.locationInfo("locInfo")
				.lineNumber("line")
				.build()));
		Assert.assertFalse(logOne.equals(new Log.Builder().categoryName("category").className("class").date("date")
				.fileName("file")
				.locationInfo("locInfo")
				.lineNumber("line")
				.methodName("method")
				.build()));
		Assert.assertFalse(logOne.equals(new Log.Builder().categoryName("category").className("class").date("date")
				.fileName("file")
				.locationInfo("locInfo")
				.lineNumber("line")
				.methodName("method")
				.level("level")
				.build()));
		Assert.assertFalse(logOne.equals(new Log.Builder().categoryName("category").className("class").date("date")
				.fileName("file")
				.locationInfo("locInfo")
				.lineNumber("line")
				.methodName("method")
				.level("level")
				.ms("ms")
				.build()));
		Assert.assertFalse(logOne.equals(new Log.Builder().categoryName("category").className("class").date("date")
				.fileName("file")
				.locationInfo("locInfo")
				.lineNumber("line")
				.methodName("method")
				.level("level")
				.ms("ms")
				.threadName("thread")
				.build()));
		Assert.assertFalse(logOne.equals(new Log.Builder().categoryName("category").className("class").date("date")
				.fileName("file")
				.locationInfo("locInfo")
				.lineNumber("line")
				.methodName("method")
				.level("level")
				.ms("ms")
				.threadName("thread")
				.message("message")
				.build()));
	}
}
