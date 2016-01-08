package net.slipp.service.qna

import org.hamcrest.CoreMatchers.is
import org.junit.Assert.assertThat
import org.junit.Test

class FacebookServiceTest {
  @Test def createLink_to_question {
    val url: String = "http://localhost:8080"
    val dut: FacebookService = new FacebookService() {
      protected override def createApplicationUrl: String = {
        return url
      }
    }
    assertThat(dut.createLink(1L), is(url + "/questions/1"))
  }

  @Test def createLink_to_answer {
    val url: String = "http://localhost:8080"
    val dut: FacebookService = new FacebookService() {
      protected override def createApplicationUrl: String = {
        return url
      }
    }
    assertThat(dut.createLink(1L, 2L), is(url + "/questions/1#answer-2"))
  }
}
