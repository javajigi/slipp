package net.slipp.repository.qna

import java.lang.Long
import java.util.List

import net.slipp.domain.qna.Question
import net.slipp.domain.user.SocialUser
import org.springframework.data.domain.{Page, Pageable}
import org.springframework.data.jpa.repository.{JpaRepository, Modifying, Query}
import org.springframework.data.querydsl.QueryDslPredicateExecutor
import org.springframework.data.repository.query.Param

trait QuestionRepository extends JpaRepository[Question, Long] {
  def findByWriter(writer: SocialUser): List[Question]

  def findByDeleted(deleted: Boolean): List[Question]

  def findByDeleted(deleted: Boolean, pageable: Pageable): Page[Question]

  def findByWriterAndDeleted(writer: SocialUser, deleted: Boolean, pageable: Pageable): Page[Question]

  @Query("SELECT q from Question q JOIN q.tags t where t.name = :name")
  def findsByTag(@Param("name") name: String, pageable: Pageable): Page[Question]

  @Modifying
  @Query("UPDATE Question q set q.showCount = q.showCount + 1 where q.questionId = :questionId")
  def updateShowCount(@Param("questionId") questionId: Long)

  @Modifying
  @Query("UPDATE Question q set q.denormalizedTags = :denormalizedTags where q.questionId = :questionId")
  def updateDenormalizedTags(@Param("questionId") questionId: Long, @Param("denormalizedTags") denormalizedTags: String)

  @Query("SELECT q from Question q JOIN q.contentsHolder c where c LIKE %:searchTerm%")
  def findsBySearch(@Param("searchTerm") searchTerm: String, pageable: Pageable): Page[Question]

  @Query("select a.question from Answer a where a.writer = :writer group by a.question order by a.question.createdDate desc")
  def findsAnswerByWriter(@Param("writer") writer: SocialUser, pageable: Pageable): Page[Question]
}
