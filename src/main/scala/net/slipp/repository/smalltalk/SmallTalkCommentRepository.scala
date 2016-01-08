package net.slipp.repository.smalltalk

import java.lang.Long
import java.util.List
import net.slipp.domain.smalltalk.SmallTalkComment
import net.slipp.domain.smalltalk.SmallTalk
import net.slipp.domain.user.SocialUser
import org.springframework.data.repository.CrudRepository

trait SmallTalkCommentRepository extends CrudRepository[SmallTalkComment, Long] {
  def findBySmallTalk(smallTalk: SmallTalk): List[SmallTalkComment]

  def findByWriter(writer: SocialUser): List[SmallTalkComment]
}