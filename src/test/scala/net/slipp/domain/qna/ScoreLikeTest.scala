package net.slipp.domain.qna

import net.slipp.domain.user.SocialUser
import net.slipp.repository.qna.ScoreLikeRepository
import net.slipp.service.rank.ScoreLikeService
import net.slipp.support.test.MockitoIntegrationTest
import org.junit.{Before, Test}
import org.mockito.{InjectMocks, Mock}

class ScoreLikeTest extends MockitoIntegrationTest {
  private var answer: Answer = null
  @Mock private var scoreLikeRepository: ScoreLikeRepository = _
  @InjectMocks private var scoreLikeService: ScoreLikeService = new ScoreLikeService

  @Before def before {
    answer = new Answer(1L)
  }

  @Test def test_랭킹점수정보저장 {
    answer.writedBy(new SocialUser(1L))
    scoreLikeService.saveLikeAnswer(1L, 1L)
  }
}
