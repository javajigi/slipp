package net.slipp.domain.qna

import java.util.Set
import net.slipp.domain.tag.Tag
import com.google.common.collect.Sets

class DifferenceTags(oldTags: Set[Tag], newTags: Set[Tag]) {
  def taggedNewTags = Sets.difference(newTags, oldTags)

  def detaggedOldTags = Sets.difference(oldTags, newTags)
}
