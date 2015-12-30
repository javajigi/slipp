package net.slipp.domain.qna

import java.util.{List, Set}

import com.google.common.collect.Lists
import net.slipp.domain.user.SocialUser
import org.hamcrest.CoreMatchers._
import org.junit.Assert._
import org.junit.Test

class AnswersTest {
  @Test def findFacebookAnswerer {
    val answer1: Answer = new Answer
    answer1.writedBy(createSocialUser(1L, "facebook"))
    val answer2: Answer = new Answer
    answer2.writedBy(createSocialUser(2L, "google"))
    val persistedAnswers: List[Answer] = Lists.newArrayList(answer1, answer2)
    val answers: Answers = new Answers(persistedAnswers)
    val answerers = answers.findFacebookAnswerers
    assertThat(answerers.size, is(1))
  }

  private def createSocialUser(id: Long, providerId: String): SocialUser = {
    val socialUser: SocialUser = new SocialUser(id)
    socialUser.setProviderId(providerId)
    return socialUser
  }
}
