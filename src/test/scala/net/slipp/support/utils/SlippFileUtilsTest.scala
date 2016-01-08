package net.slipp.support.utils

import java.io.File

import org.junit.Test

class SlippFileUtilsTest {
  @Test def forceMkParentDir(): Unit = {
    SlippFileUtils.forceMkParentDir(new File("/Users/javajigi/attachments/2016/01"))
  }
}
