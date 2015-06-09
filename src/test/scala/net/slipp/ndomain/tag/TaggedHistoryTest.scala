package net.slipp.ndomain.tag

import org.hamcrest.CoreMatchers.is
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThat
import org.junit.Test

import javax.persistence.Entity
import javax.persistence.Table
import net.slipp.domain.tag.TaggedType

class TaggedHistoryTest {
  @Test def create() {
    val tagId = 1L
    val questionId = 2L
    val userId = 3L
    val actual = new TaggedHistory(tagId, questionId, userId, TaggedType.TAGGED);
    assertThat(actual, is(new TaggedHistory(tagId, questionId, userId, TaggedType.TAGGED)));
    
    assertNotNull(new TaggedHistory)
  }
}