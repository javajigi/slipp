package net.slipp.repository.qna

import java.lang.Long
import java.util.List

import net.slipp.domain.qna.Answer
import net.slipp.domain.user.SocialUser
import org.springframework.data.jpa.repository.JpaRepository

trait AnswerRepository extends JpaRepository[Answer, Long] {
  def findByWriter(writer: SocialUser): List[Answer]
}