package net.slipp.repository.qna

import java.lang.Long
import net.slipp.domain.qna.ScoreLike
import net.slipp.domain.qna.ScoreLikeType
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

trait ScoreLikeRepository extends CrudRepository[ScoreLike, Long] {
  @Query("select o from ScoreLike o where o.socialUserId = :socialUserId and o.scoreLikeType = :scoreLikeType and o.targetId = :targetId and o.liked = true")
  def findBySocialUserIdAndLike(@Param("scoreLikeType") scoreLikeType: ScoreLikeType, @Param("targetId") targetId: Long, @Param("socialUserId") socialUserId: Long): ScoreLike

  @Query("select o from ScoreLike o where o.socialUserId = :socialUserId and o.scoreLikeType = :scoreLikeType and o.targetId = :targetId and o.liked = false")
  def findBySocialUserIdAndDisLike(@Param("scoreLikeType") scoreLikeType: ScoreLikeType, @Param("targetId") targetId: Long, @Param("socialUserId") socialUserId: Long): ScoreLike
}
