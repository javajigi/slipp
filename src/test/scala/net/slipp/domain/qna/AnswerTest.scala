package net.slipp.domain.qna

import net.slipp.domain.qna.AnswerBuilder._
import org.hamcrest.CoreMatchers._
import org.junit.Assert._
import org.junit.Test

class AnswerTest {
  @Test def likedMoreThan {
    val answer: Answer = anAnswer.withTotalLiked(2).build
    assertThat(answer.likedMoreThan(2), is(true))
  }

  @Test def isNotBest {
    val answer: Answer = anAnswer.withTotalLiked(1).build
    assertThat(answer.likedMoreThan(2), is(false))
  }
}