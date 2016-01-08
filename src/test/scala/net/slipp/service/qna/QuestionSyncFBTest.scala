package net.slipp.service.qna

import org.hamcrest.CoreMatchers.is
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import com.restfb.DefaultFacebookClient
import com.restfb.FacebookClient
import com.restfb.Parameter
import com.restfb.Version
import com.restfb.types.Comment
import com.restfb.types.FacebookType
import com.restfb.types.Post
import com.restfb.types.Post.Comments

@Ignore
@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(Array("classpath:test-applicationContext.xml"))
class QuestionSyncFBTest {
  private var log: Logger = LoggerFactory.getLogger(classOf[QuestionSyncFBTest])

  @Value("#{applicationProperties['my.facebook.accessToken']}") private var myAccessToken: String = null
  private var dut: FacebookClient = null

  @Before def setup {
    dut = new DefaultFacebookClient(myAccessToken, Version.VERSION_2_2)
  }

  @Test
  @throws(classOf[Exception])
  def post {
    val message: String = "글쓰기 테스트입니다."
    val response: FacebookType = dut.publish("me/feed", classOf[FacebookType], Parameter.`with`("link", "http://www.slipp.net/questions/87"), Parameter.`with`("message", message))
    val id: String = response.getId
    log.debug("id : {}", id)
    val post: Post = dut.fetchObject(id, classOf[Post])
    assertThat(post.getMessage, is(message))
  }

  @Test
  @throws(classOf[Exception])
  def findComments {
    val post: Post = dut.fetchObject("1324855987_4834614866310", classOf[Post])
    val comments: Post.Comments = post.getComments
    import scala.collection.JavaConversions._
    for (comment <- comments.getData) {
      log.debug("comment : {}", comment)
    }
  }
}
