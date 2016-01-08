package net.slipp.domain.tag

import org.hamcrest.CoreMatchers.is
import org.junit.Assert.assertThat
import org.junit.Test

class TagInfoTest {
  @Test
  def isConnectGroup {
    val dut: TagInfo = new TagInfo("1234", "description")
    assertThat(dut.isConnectGroup, is(true))
  }

  @Test
  def isNotConnectGroup {
    val dut: TagInfo = new TagInfo
    assertThat(dut.isConnectGroup, is(false))
  }

  @Test def getGroupUrl {
    val dut: TagInfo = new TagInfo("1234", "description")
    assertThat(dut.getGroupUrl, is("https://www.facebook.com/groups/1234"))
  }

  @Test def getGroupUrlGroupIdIsEmpty {
    val dut: TagInfo = new TagInfo
    assertThat(dut.getGroupUrl, is(""))
  }
}
