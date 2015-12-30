package net.slipp.repository.smalltalk

import java.lang.Long
import java.util.List

import net.slipp.domain.user.SocialUser
import net.slipp.domain.smalltalk.SmallTalk
import org.springframework.data.repository.PagingAndSortingRepository

trait SmallTalkRepository extends PagingAndSortingRepository[SmallTalk, Long] {
  def findByWriter(writer: SocialUser): List[SmallTalk]
}