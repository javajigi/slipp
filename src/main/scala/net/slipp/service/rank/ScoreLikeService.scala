package net.slipp.service.rank

import net.slipp.domain.qna.{ScoreLike, ScoreLikeType}
import net.slipp.repository.qna.ScoreLikeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
  * 향후 질문(Quest)와의 동적 처리
  *
  * @author eclipse4j
  *
  */
@Service class ScoreLikeService {
  @Autowired private var scoreLikeRepository: ScoreLikeRepository = null

  def alreadyLikedQuestion(questionId: Long, socialUserId: Long): Boolean = {
    val scoreLike: ScoreLike = scoreLikeRepository.findBySocialUserIdAndLike(ScoreLikeType.QUESTION, questionId, socialUserId)
    Option(scoreLike) match {
      case Some(_) => true
      case None => false
    }
  }

  def alreadyLikedAnswer(answerId: Long, socialUserId: Long): Boolean = {
    val scoreLike: ScoreLike = scoreLikeRepository.findBySocialUserIdAndLike(ScoreLikeType.ANSWER, answerId, socialUserId)
    Option(scoreLike) match {
      case Some(_) => true
      case None => false
    }
  }

  def alreadyDisLikedAnswer(answerId: Long, socialUserId: Long): Boolean = {
    val scoreLike: ScoreLike = scoreLikeRepository.findBySocialUserIdAndDisLike(ScoreLikeType.ANSWER, answerId, socialUserId)
    Option(scoreLike) match {
      case Some(_) => true
      case None => false
    }
  }

  def saveLikeAnswer(answerId: Long, socialUserId: Long) {
    scoreLikeRepository.save(ScoreLike.createLikedScoreLike(ScoreLikeType.ANSWER, socialUserId, answerId))
  }

  def saveDisLikeAnswer(answerId: Long, socialUserId: Long) {
    scoreLikeRepository.save(ScoreLike.createDisLikedScoreLike(ScoreLikeType.ANSWER, socialUserId, answerId))
  }

  def saveLikeQuestion(questionId: Long, socialUserId: Long) {
    scoreLikeRepository.save(ScoreLike.createLikedScoreLike(ScoreLikeType.QUESTION, socialUserId, questionId))
  }
}
