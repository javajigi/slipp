package net.slipp.domain.qna

import java.util.Set

import com.google.common.collect.Sets
import net.slipp.domain.tag.Tag
import org.hamcrest.CoreMatchers._
import org.junit.Assert._
import org.junit.Test

class TagHelperTest {
  @Test def denormalizedTags {
    val tags: Set[Tag] = Sets.newHashSet(Tag.newTag("java"), Tag.newTag("javascript"))
    val actual: String = TagHelper.denormalizedTags(tags)
    assertThat(actual, is("java,javascript"))
  }
}
