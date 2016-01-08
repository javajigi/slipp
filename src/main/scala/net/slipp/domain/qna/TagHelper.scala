package net.slipp.domain.qna

import java.util.Set
import com.google.common.base.Function
import com.google.common.base.Joiner
import com.google.common.collect.Collections2
import net.slipp.domain.tag.Tag

object TagHelper {
  def denormalizedTags(tags: Set[Tag]): String = {
    if (tags == null) {
      return null
    }
    val tagToString: Function[Tag, String] = new Function[Tag, String]() {
      def apply(input: Tag): String = {
        return input.getName
      }
    }
    return Joiner.on(",").join(Collections2.transform(tags, tagToString))
  }
}

