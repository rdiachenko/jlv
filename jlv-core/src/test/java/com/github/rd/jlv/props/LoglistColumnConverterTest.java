package com.github.rd.jlv.props;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class LoglistColumnConverterTest {

	private PropertyConverter<List<LoglistColumn>> converter = new LoglistColumnConverter();

	@Test
	public void testConvertFromString() {
		LoglistColumn[] expected = {
				new LoglistColumn("Level", true, 55),
				new LoglistColumn("Category", false, 200)
		};
		String value = " Level : true : 55 ;  Category: false:200;";
		List<LoglistColumn> converted = converter.convertFromString(value);
		LoglistColumn[] actual = converted.toArray(new LoglistColumn[converted.size()]);

		Assert.assertArrayEquals(expected, actual);
	}

	@Test
	public void testConvertToString() {
		List<LoglistColumn> value = new ArrayList<>();
		value.add(new LoglistColumn("Level", true, 55));
		value.add(new LoglistColumn("Category", false, 200));
		Assert.assertEquals("Level:true:55;Category:false:200;", converter.convertToString(value));
	}
}
