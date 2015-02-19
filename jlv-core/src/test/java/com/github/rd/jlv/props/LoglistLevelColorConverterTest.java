package com.github.rd.jlv.props;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class LoglistLevelColorConverterTest {

	private PropertyConverter<List<LoglistLevelColor>> converter = new LoglistLevelColorConverter();
	
	@Test
	public void testConvertFromString() {
		LoglistLevelColor[] expected = {
				new LoglistLevelColor("DEBUG", new LevelColor(0, 2, 3), new LevelColor(255, 254, 5)),
				new LoglistLevelColor("INFO", new LevelColor(17, 25, 1), new LevelColor(22, 34, 77))	
		};
		String value = " DEBUG : 0 - 2 - 3 : 255 - 254 - 5 ; INFO:17-25-1:22-34-77 ; ";
		List<LoglistLevelColor> converted = converter.convertFromString(value);
		LoglistLevelColor[] actual = converted.toArray(new LoglistLevelColor[converted.size()]);
		
		Assert.assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testConvertToString() {
		List<LoglistLevelColor> value = new ArrayList<>();
		value.add(new LoglistLevelColor("DEBUG", new LevelColor(0, 2, 3), new LevelColor(255, 254, 5)));
		value.add(new LoglistLevelColor("INFO", new LevelColor(17, 25, 1), new LevelColor(22, 34, 77)));
		Assert.assertEquals("DEBUG:0-2-3:255-254-5;INFO:17-25-1:22-34-77;", converter.convertToString(value));
	}
}
