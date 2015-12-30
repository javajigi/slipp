package net.slipp.ndomain.tag

import net.slipp.domain.tag.{TaggedHistory, TaggedType}
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.{assertNotNull, assertThat}
import org.junit.Test

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