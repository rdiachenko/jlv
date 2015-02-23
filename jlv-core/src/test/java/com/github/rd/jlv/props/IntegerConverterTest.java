package com.github.rd.jlv.props;

import org.junit.Assert;
import org.junit.Test;

public class IntegerConverterTest {

	private PropertyConverter<Integer> converter = new IntegerConverter();

	@Test
	public void testConvertFromString() {
		Assert.assertEquals(7, (int) converter.convertFromString("  7"));
	}

	@Test
	public void testConvertToString() {
		Assert.assertEquals("7", converter.convertToString(7));
	}
}
