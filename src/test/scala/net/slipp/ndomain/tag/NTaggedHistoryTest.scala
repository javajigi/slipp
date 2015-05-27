package net.slipp.ndomain.tag

import org.junit.Assert._
import org.hamcrest.CoreMatchers._
import org.junit.Test
import net.slipp.domain.tag.TaggedType

class NTaggedHistoryTest {
  @Test def create() {
    val tagId = 1L
    val questionId = 2L
    val userId = 3L
    val actual = new NTaggedHistory(tagId, questionId, userId, TaggedType.TAGGED);
    assertThat(actual, is(new NTaggedHistory(tagId, questionId, userId, TaggedType.TAGGED)));
    
    assertNotNull(new NTaggedHistory)
  }
}