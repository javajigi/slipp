package net.slipp.domain.tag

import java.lang.Long

object TagBuilder {
  def aTag: TagBuilder = {
    return new TagBuilder
  }
}

class TagBuilder {
  private var id: Long = null
  private var name: String = null
  private var taggedCount: Int = 0
  private var parentTag: Tag = null
  private var groupId: String = null

  def withId(id: Long): TagBuilder = {
    this.id = id
    return this
  }

  def withName(name: String): TagBuilder = {
    this.name = name
    return this
  }

  def withParentTag(parentTag: Tag): TagBuilder = {
    this.parentTag = parentTag
    return this
  }

  def withTaggedCount(taggedCount: Int): TagBuilder = {
    this.taggedCount = taggedCount
    return this
  }

  def withGroupId(groupId: String): TagBuilder = {
    this.groupId = groupId
    return this
  }

  def build: Tag = {
    val tagInfo: TagInfo = new TagInfo(groupId, null)
    val tag: Tag = new Tag(name, parentTag, tagInfo) {
      override def getTagId: Long = {
        return id
      }
    }

    (0 to taggedCount).foreach(_ => tag.tagged())
    tag
  }
}
