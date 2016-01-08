package net.slipp.domain.tag

import org.junit.Assert.assertTrue
import java.util.Set
import org.junit.Test
import com.google.common.collect.Sets

class TagsTest {
  @Test def getConnectedGroupTag {
    val java1: Tag = Tag.pooledTag("java")
    val connectedTag: Tag = Tag.groupedTag("SLiPP", "1234567")
    val tagSet: Set[Tag] = Sets.newHashSet(java1, connectedTag)
    val tags: Tags = new Tags(tagSet)
    val groupTags: Set[Tag] = tags.getConnectedGroupTags
    assertTrue(groupTags.contains(connectedTag))
  }

  @Test def getConnectedGroupTagHasNotConntectedGroup {
    val java1: Tag = Tag.pooledTag("java")
    val c: Tag = Tag.pooledTag("c")
    val tagSet: Set[Tag] = Sets.newHashSet(java1, c)
    val tags: Tags = new Tags(tagSet)
    val groupTags: Set[Tag] = tags.getConnectedGroupTags
    assertTrue(groupTags.isEmpty)
  }

  @Test def getConnectedGroupTagEmptyTag {
    val tagSet = Sets.newHashSet[Tag]
    val tags: Tags = new Tags(tagSet)
    val groupTags: Set[Tag] = tags.getConnectedGroupTags
    assertTrue(groupTags.isEmpty)
  }
}
