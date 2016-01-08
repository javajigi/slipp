package net.slipp.domain.qna

import com.google.common.collect.Sets
import net.slipp.support.test.UnitTest
import org.junit.Assert._
import org.junit.Test

class TagHelperTest extends UnitTest {
  @Test def denormalizedTags {
    val tag1 = aSomeTag(name = "spring")
    val tag2 = aSomeTag(name = "javascript")
    val tags = Sets.newHashSet(tag1, tag2)
    val actual = TagHelper.denormalizedTags(tags)
    assertTrue(actual.contains(tag1.getName))
    assertTrue(actual.contains(tag2.getName))
  }
}