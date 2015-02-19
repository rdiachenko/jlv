package com.github.rd.jlv.props;

import org.junit.Assert;
import org.junit.Test;

public class BooleanConverterTest {

	private PropertyConverter<Boolean> converter = new BooleanConverter();
	
	@Test
	public void testConvertFromString() {
		Assert.assertTrue(converter.convertFromString(" true "));
		Assert.assertFalse(converter.convertFromString("false  "));
	}
	
	@Test
	public void testConvertToString() {
		Assert.assertEquals("true", converter.convertToString(true));
		Assert.assertEquals("false", converter.convertToString(false));
	}
}
