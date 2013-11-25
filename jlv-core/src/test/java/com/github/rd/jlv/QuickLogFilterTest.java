package com.github.rd.jlv;

import org.junit.Assert;
import org.junit.Test;

public class QuickLogFilterTest {

	@Test
	public void testSinglelineMessage() {
		String message = "java.lang.ArrayIndexOutOfBoundsException: 3 at com.rdiachenko.jlv.log4j.socketappender.ClientRunner.main(ClientRunner.java:20)";
		String regex = "(?i)(?s).*( 3 at).*";
		Assert.assertTrue(message.matches(regex));
	}

	@Test
	public void testMultilineMessage() {
		String message = "java.lang.ArrayIndexOutOfBoundsException: 3\n\n\r"
				+ "at com.rdiachenko.jlv.log4j.socketappender.ClientRunner.main(ClientRunner.java:20)";
		String regex = "(?i)(?s).*(lang).*";
		Assert.assertTrue(message.matches(regex));
	}

	@Test
	public void testCaseInsensitiveMessage() {
		String message = "java.lang.ArrayIndexOutOfBoundsException: 3\n\n\r"
				+ "at com.^%rdiachenko.*jlv.?log4j.$socketappender.ClientRunner.main(ClientRunner.java:20)";
		String regex = "(?i)(?s).*(INDEX).*";
		Assert.assertTrue(message.matches(regex));
	}
}
