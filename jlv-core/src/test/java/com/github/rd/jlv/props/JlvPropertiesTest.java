package com.github.rd.jlv.props;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.eventbus.Subscribe;

public class JlvPropertiesTest {

	private static final File TEST_FILE = new File(System.getProperty("java.io.tmpdir", "src/test/resources")
			+ "/jlv.properties");

	@BeforeClass
	public static void init() throws IOException {
		String[] properties = {
				"jlv.loglist.column=Line:true:77;Date:false:99;",
				"jlv.server.port=4446",
				"jlv.loglist.quicksearch=false",
				"jlv.server.autostart=false",
				"jlv.loglist.fontsize=14",
				"jlv.loglist.refreshtime=100",
				"jlv.loglist.buffersize=7000",
				"jlv.loglist.levelimage=false",
				"jlv.loglist.levelcolor=WARN:1-2-3:4-5-6;ERROR:7-8-9:253-254-255;",
		};
		try (PrintWriter out = new PrintWriter(TEST_FILE)) {
			for (String prop : properties) {
				out.println(prop);
			}
		}
	}

	@AfterClass
	public static void cleanUp() {
		Assert.assertTrue("test file couldn't be deleted", TEST_FILE.delete());
	}

	@Test
	public void testLoadDefault() {
		JlvProperties store = new JlvProperties(null);
		Assert.assertEquals(Integer.valueOf(4445), store.loadDefault(PropertyKey.SERVER_PORT_KEY));
		Assert.assertEquals(true, store.loadDefault(PropertyKey.SERVER_AUTOSTART_KEY));
		Assert.assertEquals(Integer.valueOf(10000), store.loadDefault(PropertyKey.LOGLIST_BUFFER_SIZE_KEY));
		Assert.assertEquals(Integer.valueOf(500), store.loadDefault(PropertyKey.LOGLIST_REFRESH_TIME_KEY));
		Assert.assertEquals(true, store.loadDefault(PropertyKey.LOGLIST_QUICK_SEARCH_KEY));
		Assert.assertEquals(Integer.valueOf(11), store.loadDefault(PropertyKey.LOGLIST_FONT_SIZE_KEY));
		Assert.assertEquals(true, store.loadDefault(PropertyKey.LOGLIST_LEVEL_IMAGE_KEY));

		LoglistColumn[] expectedColumns = {
				new LoglistColumn("Level", true, 55),
				new LoglistColumn("Category", true, 100),
				new LoglistColumn("Message", true, 100),
				new LoglistColumn("Line", true, 100),
				new LoglistColumn("Date", true, 100),
				new LoglistColumn("Throwable", true, 100)
		};
		List<LoglistColumn> columns = store.loadDefault(PropertyKey.LOGLIST_COLUMN_KEY);
		LoglistColumn[] actualColumns = columns.toArray(new LoglistColumn[columns.size()]);
		Assert.assertArrayEquals(expectedColumns, actualColumns);

		LoglistLevelColor[] expectedLevelColors = {
				new LoglistLevelColor("DEBUG", new LevelColor(0, 0, 0), new LevelColor(255, 255, 255)),
				new LoglistLevelColor("INFO", new LevelColor(0, 255, 0), new LevelColor(255, 255, 255)),
				new LoglistLevelColor("WARN", new LevelColor(0, 128, 0), new LevelColor(255, 255, 255)),
				new LoglistLevelColor("ERROR", new LevelColor(255, 0, 0), new LevelColor(255, 255, 255)),
				new LoglistLevelColor("FATAL", new LevelColor(165, 42, 42), new LevelColor(255, 255, 255))
		};
		List<LoglistLevelColor> levelColors = store.loadDefault(PropertyKey.LOGLIST_LEVEL_COLOR_KEY);
		LoglistLevelColor[] actualLevelColors = levelColors.toArray(new LoglistLevelColor[levelColors.size()]);
		Assert.assertArrayEquals(expectedLevelColors, actualLevelColors);
	}

	@Test
	public void testLoadFromFile() {
		JlvProperties store = new JlvProperties(TEST_FILE);
		Assert.assertEquals(Integer.valueOf(4446), store.load(PropertyKey.SERVER_PORT_KEY));
		Assert.assertEquals(false, store.load(PropertyKey.SERVER_AUTOSTART_KEY));
		Assert.assertEquals(Integer.valueOf(7000), store.load(PropertyKey.LOGLIST_BUFFER_SIZE_KEY));
		Assert.assertEquals(Integer.valueOf(100), store.load(PropertyKey.LOGLIST_REFRESH_TIME_KEY));
		Assert.assertEquals(false, store.load(PropertyKey.LOGLIST_QUICK_SEARCH_KEY));
		Assert.assertEquals(Integer.valueOf(14), store.load(PropertyKey.LOGLIST_FONT_SIZE_KEY));
		Assert.assertEquals(false, store.load(PropertyKey.LOGLIST_LEVEL_IMAGE_KEY));

		LoglistColumn[] expectedColumns = {
				new LoglistColumn("Line", true, 77),
				new LoglistColumn("Date", false, 99)
		};
		List<LoglistColumn> columns = store.load(PropertyKey.LOGLIST_COLUMN_KEY);
		LoglistColumn[] actualColumns = columns.toArray(new LoglistColumn[columns.size()]);
		Assert.assertArrayEquals(expectedColumns, actualColumns);

		LoglistLevelColor[] expectedLevelColors = {
				new LoglistLevelColor("WARN", new LevelColor(1, 2, 3), new LevelColor(4, 5, 6)),
				new LoglistLevelColor("ERROR", new LevelColor(7, 8, 9), new LevelColor(253, 254, 255))
		};
		List<LoglistLevelColor> levelColors = store.load(PropertyKey.LOGLIST_LEVEL_COLOR_KEY);
		LoglistLevelColor[] actualLevelColors = levelColors.toArray(new LoglistLevelColor[levelColors.size()]);
		Assert.assertArrayEquals(expectedLevelColors, actualLevelColors);
	}

	@Test
	public void testSaveToFile() {
		JlvProperties store = new JlvProperties(TEST_FILE);
		int oldPort = store.load(PropertyKey.SERVER_PORT_KEY);
		boolean oldQuickSearch = store.load(PropertyKey.LOGLIST_QUICK_SEARCH_KEY);
		Assert.assertEquals(4446, oldPort);
		Assert.assertEquals(false, oldQuickSearch);

		int newPort = 7777;
		boolean newQuickSearch = true;
		store.save(PropertyKey.SERVER_PORT_KEY, newPort);
		store.save(PropertyKey.LOGLIST_QUICK_SEARCH_KEY, newQuickSearch);
		store.persist();

		// load updated properties file
		store = new JlvProperties(TEST_FILE);
		Assert.assertEquals(Integer.valueOf(newPort), store.load(PropertyKey.SERVER_PORT_KEY));
		Assert.assertEquals(newQuickSearch, store.load(PropertyKey.LOGLIST_QUICK_SEARCH_KEY));

		// revert changes in properties file
		store.save(PropertyKey.SERVER_PORT_KEY, oldPort);
		store.save(PropertyKey.LOGLIST_QUICK_SEARCH_KEY, oldQuickSearch);
		store.persist();

		// load reverted properties file
		store = new JlvProperties(TEST_FILE);
		Assert.assertEquals(Integer.valueOf(oldPort), store.load(PropertyKey.SERVER_PORT_KEY));
		Assert.assertEquals(oldQuickSearch, store.load(PropertyKey.LOGLIST_QUICK_SEARCH_KEY));
	}

	@Test
	public void testPropertyChange() {
		JlvProperties store = new JlvProperties(null);

		class PropertyChangeListener {
			@Subscribe
			public void propertyChange(PropertyChangeEvent event) {
				Assert.assertEquals(EventScope.CONFIGURATION, event.getScope());
				Assert.assertEquals(PropertyKey.LOGLIST_LEVEL_COLOR_KEY, event.getProperty());
				Assert.assertFalse("property old and new values are the same",
						event.getOldValue().equals(event.getNewValue()));
			}
		}
		PropertyChangeListener listener = new PropertyChangeListener();
		store.addPropertyChangeListener(listener);

		List<LoglistLevelColor> oldValue = store.load(PropertyKey.LOGLIST_LEVEL_COLOR_KEY);
		List<LoglistLevelColor> newValue = Arrays.asList(new LoglistLevelColor("WARN", new LevelColor(1, 2, 3),
				new LevelColor(4, 5, 6)));
		store.save(PropertyKey.LOGLIST_LEVEL_COLOR_KEY, newValue);
		store.firePropertyChangeEvent(PropertyKey.LOGLIST_LEVEL_COLOR_KEY, oldValue, newValue, EventScope.CONFIGURATION);
		store.removePropertyChangeListener(listener);
	}
}
