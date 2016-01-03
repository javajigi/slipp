package net.slipp.domain.qna

import net.slipp.domain.qna.AnswerBuilder._
import net.slipp.support.test.UnitTest
import org.hamcrest.CoreMatchers._
import org.junit.Assert._
import org.junit.Test

class AnswerTest extends UnitTest {
  @Test def likedMoreThan {
    val answer = aSomeAnswer(sumLiked = 2)
    assertThat(answer.likedMoreThan(2), is(true))
  }

  @Test def isNotBest {
    val answer = aSomeAnswer(sumLiked = 1)
    assertThat(answer.likedMoreThan(2), is(false))
  }
}