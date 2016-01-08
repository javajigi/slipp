package net.slipp.domain.tag

import net.slipp.domain.tag.TagBuilder.aTag
import org.hamcrest.CoreMatchers.is
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.assertThat
import org.junit.Test

object TagTest {
  val JAVA: Tag = Tag.pooledTag("java")
  val JAVA_CHILD: Tag = Tag.pooledTag("자바", JAVA)
  val JAVASCRIPT: Tag = Tag.pooledTag("javascript")
  val NEWTAG: Tag = Tag.newTag("newTag")
}

class TagTest {
  @Test def equalsAndContainsTag {
    val java1: Tag = aTag.withName("java").build
    val java2: Tag = aTag.withName("java").build
    assertThat(java1, is(java2))
  }

  @Test
  @throws(classOf[Exception])
  def addChild {
    val actual: Tag = TagTest.JAVA_CHILD.getParent
    assertThat(actual, is(TagTest.JAVA))
  }

  @Test
  @throws(classOf[Exception])
  def getRevisedTag_parentTag {
    assertThat(TagTest.JAVA.getRevisedTag, is(TagTest.JAVA))
  }

  @Test
  @throws(classOf[Exception])
  def getRevisedTag_childTag {
    assertThat(TagTest.JAVA_CHILD.getRevisedTag, is(TagTest.JAVA))
  }

  @Test def tagged {
    val dut: Tag = Tag.pooledTag("java")
    dut.tagged
    assertThat(dut.getTaggedCount, is(1))
  }

  @Test
  @throws(classOf[Exception])
  def deTagged {
    val dut: Tag = Tag.pooledTag("java")
    dut.tagged
    dut.tagged
    assertThat(dut.getTaggedCount, is(2))
    dut.deTagged
    assertThat(dut.getTaggedCount, is(1))
  }

  @Test def createNewTagFromQuestion {
    val name: String = "newTag"
    val dut: Tag = Tag.newTag(name)
    assertThat(dut.isPooled, is(true))
    assertThat(dut.getName, is(name.toLowerCase))
    assertThat(dut.getParent, is(nullValue))
  }

  @Test def movePooled {
    var parent: Tag = null
    var newTag: Tag = Tag.newTag("newTag")
    newTag.movePooled(parent)
    assertThat(newTag.isPooled, is(true))
    parent = Tag.pooledTag("java")
    newTag = Tag.newTag("newTag")
    newTag.movePooled(parent)
    assertThat(newTag.isPooled, is(true))
    assertThat(newTag.getParent, is(parent))
  }

  @Test def moveGroupTag {
    val name = "newtag"
    val groupId = "1234"
    val newTag: Tag = Tag.newTag(name)
    newTag.moveGroupTag(groupId)
    assertThat(newTag.getGroupId, is(groupId))

    val pooledTag: Tag = Tag.pooledTag(name)
    pooledTag.moveGroupTag(groupId)
    assertThat(pooledTag.getGroupId, is(groupId))
  }
}
