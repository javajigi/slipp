package net.slipp.service.rank

import net.slipp.domain.qna.ScoreLikeType
import org.junit.Assert._

import net.slipp.repository.qna.ScoreLikeRepository
import net.slipp.support.test.MockitoIntegrationTest
import org.junit.Test
import org.mockito.{InjectMocks, Mock}
import org.mockito.Mockito.when

class ScoreLikeServiceTest extends MockitoIntegrationTest {
  @Mock var scoreLikeRepository: ScoreLikeRepository = _
  @InjectMocks val scoreLikeService: ScoreLikeService = new ScoreLikeService

  @Test def alreadyLikedQuestion(): Unit = {
    when(scoreLikeRepository.findBySocialUserIdAndLike(ScoreLikeType.QUESTION, 1L, 1L)).thenReturn(aSomeQuestionScoreLike())
    assertTrue(scoreLikeService.alreadyLikedQuestion(1L, 1L))

    when(scoreLikeRepository.findBySocialUserIdAndLike(ScoreLikeType.QUESTION, 1L, 1L)).thenReturn(null)
    assertFalse(scoreLikeService.alreadyLikedQuestion(1L, 1L))
  }

  @Test def alreadyLikedAnswer(): Unit = {
    when(scoreLikeRepository.findBySocialUserIdAndLike(ScoreLikeType.ANSWER, 1L, 1L)).thenReturn(aSomeAnswerScoreLike())
    assertTrue(scoreLikeService.alreadyLikedAnswer(1L, 1L))

    when(scoreLikeRepository.findBySocialUserIdAndLike(ScoreLikeType.ANSWER, 1L, 1L)).thenReturn(null)
    assertFalse(scoreLikeService.alreadyLikedAnswer(1L, 1L))
  }

  @Test def alreadyDisLikedAnswer(): Unit = {
    when(scoreLikeRepository.findBySocialUserIdAndLike(ScoreLikeType.ANSWER, 1L, 1L)).thenReturn(aSomeAnswerScoreDisLike())
    assertTrue(scoreLikeService.alreadyLikedAnswer(1L, 1L))

    when(scoreLikeRepository.findBySocialUserIdAndLike(ScoreLikeType.ANSWER, 1L, 1L)).thenReturn(null)
    assertFalse(scoreLikeService.alreadyLikedAnswer(1L, 1L))
  }
}
