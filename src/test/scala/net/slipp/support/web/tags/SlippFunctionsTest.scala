package net.slipp.support.web.tags

import org.hamcrest.CoreMatchers.is
import org.junit.Assert.assertThat
import net.slipp.domain.user.SocialUser
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SlippFunctionsTest {
  private val logger: Logger = LoggerFactory.getLogger(classOf[SlippFunctionsTest])

  @Test def isWriter {
    val actual: Boolean = SlippFunctions.isWriter(new SocialUser(10), new SocialUser(10))
    assertThat(actual, is(true))
  }

  @Test
  @throws(classOf[Exception])
  def isNotWriter {
    var actual: Boolean = SlippFunctions.isWriter(new SocialUser(10), new SocialUser(11))
    assertThat(actual, is(false))
    actual = SlippFunctions.isWriter(null, new SocialUser(11))
    assertThat(actual, is(false))
  }

  @Test
  @throws(classOf[Exception])
  def wiki {
    val source: String = "{code:title=java}\n WikiContents wikiContents = new WikiContents();{code}\n" + "!1234!\n !2345!"
    val actual: String = SlippFunctions.wiki(source)
    logger.debug("convert wiki contents : {}", actual)
  }

  @Test
  @throws(classOf[Exception])
  def stripHttp {
    val url: String = "http://localhost:8080"
    val actual: String = SlippFunctions.stripHttp(url)
    assertThat(actual, is("//localhost:8080"))
  }

  @Test
  @throws(classOf[Exception])
  def links {
    val message: String = "길기용 이가 이렇게 정리했었던 기억이... ^^\n" + "http://www.slipp.net/wiki/display/SLS/mustache#mustache-잉여내용:Spring에서Handlebars를쓴다면?\n" + "이항희 님의 http://blog.javarouka.me/2014/08/handlebars-for-java_31.html"
    val actual: String = SlippFunctions.links(message)
    logger.debug("links message : {}", actual)
  }
}
