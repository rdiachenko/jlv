package com.rdiachenko.jlv;

import org.junit.Assert;
import org.junit.Test;

public class LogUtilsTest {

  @Test
  public void nullToEmptyTest() {
    Assert.assertTrue(LogUtils.nullToEmpty(null).isEmpty());
    Assert.assertEquals("test", LogUtils.nullToEmpty("test"));
  }

  @Test
  public void formatDateTest() {
    long timestamp = 1486505215127L;
    Assert.assertEquals("2017-02-07 22:06:55.127", LogUtils.formatDate(timestamp));
  }
}
