package net.slipp.domain.qna

import java.util.Set

import com.google.common.collect.Sets
import net.slipp.domain.tag.Tag
import org.hamcrest.CoreMatchers._
import org.junit.Assert.assertThat
import org.junit.Test

class DifferenceTagsTest {
  @Test def differenceTag {
    val oldTags: Set[Tag] = Sets.newHashSet(Tag.newTag("java"), Tag.newTag("javascript"))
    val newTags: Set[Tag] = Sets.newHashSet(Tag.newTag("java"), Tag.newTag("자바"))
    val tags: DifferenceTags = new DifferenceTags(oldTags, newTags)
    val taagedNewTags: Set[Tag] = Sets.newHashSet(Tag.newTag("자바"))
    val detaggedOldTags: Set[Tag] = Sets.newHashSet(Tag.newTag("javascript"))
    assertThat(tags.taggedNewTags, is(taagedNewTags))
    assertThat(tags.detaggedOldTags, is(detaggedOldTags))
  }
}
