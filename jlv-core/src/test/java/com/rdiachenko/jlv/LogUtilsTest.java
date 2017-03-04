package com.rdiachenko.jlv;

import org.junit.Assert;
import org.junit.Test;

public class LogUtilsTest {

  @Test
  public void nullToEmptyTest() {
    Assert.assertTrue(LogUtils.nullToEmpty(null).isEmpty());
    Assert.assertEquals("test", LogUtils.nullToEmpty("test"));
  }
}
