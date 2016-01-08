package net.slipp.domain.qna

import java.util.Set

import net.slipp.domain.fb.FacebookGroup
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.{assertThat, assertTrue}
import org.junit.Test

class QuestionDtoTest {
  @Test def createFacebookGroups {
    val fbGroups: Array[String] = Array("12345::생활코딩", "67890::SLiPP 스터디 1기")
    val groups: Set[FacebookGroup] = QuestionDto.createFacebookGroups(fbGroups)
    assertTrue(groups.contains(new FacebookGroup("12345", "생활코딩")))
    assertTrue(groups.contains(new FacebookGroup("67890", "SLiPP-스터디-1기")))
  }

  @Test def createFacebookGroup {
    val group: FacebookGroup = QuestionDto.createFacebookGroup("12345::생활코딩")
    assertThat(group, is(new FacebookGroup("12345", "생활코딩")))
  }

  @Test
  @throws(classOf[Exception])
  def replaceSpaceToUnderbar {
    val actual: String = QuestionDto.replaceSpaceToDash("SLiPP 스터디 1기")
    assertThat(actual, is("SLiPP-스터디-1기"))
  }
}
