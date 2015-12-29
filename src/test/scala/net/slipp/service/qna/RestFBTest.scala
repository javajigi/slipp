package net.slipp.service.qna

import java.util.List

import com.restfb.types.{Comment, FacebookType, Group, Post}
import com.restfb.{Connection, DefaultFacebookClient, FacebookClient, Parameter, Version}
import net.slipp.domain.fb.FacebookComment
import org.junit.{Before, Ignore, Test}
import org.slf4j.{Logger, LoggerFactory}

@Ignore
class RestFBTest {
  private var logger: Logger = LoggerFactory.getLogger(classOf[RestFBTest])

  private var dut: FacebookClient = null

  @Before def setup {
    val accessToken: String = "ACCESS_TOKEN"
    dut = new DefaultFacebookClient(accessToken, Version.VERSION_2_3)
  }

  @Test
  @throws(classOf[Exception])
  def post {
    val message: String = "그룹에 글쓰기 테스트입니다."
    val response: FacebookType = dut.publish("me/feed", classOf[FacebookType], Parameter.`with`("link", "http://www.slipp.net/questions/87"), Parameter.`with`("message", message))
    val id: String = response.getId
    logger.debug("id : {}", id)
  }

  @Test
  @throws(classOf[Exception])
  def groupPost {
    val message: String = "그룹에 글쓰기 테스트입니다."
    val response: FacebookType = dut.publish("387530111326987/feed", classOf[FacebookType], Parameter.`with`("link", "http://www.slipp.net/questions/87"), Parameter.`with`("message", message))
    val id: String = response.getId
    logger.debug("id : {}", id)
  }

  @Test
  @throws(classOf[Exception])
  def fetchPost {
    val post: Post = dut.fetchObject("10207006906754177", classOf[Post])
    logger.debug("Post: " + post.getId + " : " + post.getMessage)
    val comments: Post.Comments = post.getComments
    val commentData: List[Comment] = comments.getData
    logger.debug("comment size : {}", commentData.size)
    import scala.collection.JavaConversions._
    for (comment <- commentData) {
      val fbComment: FacebookComment = FacebookComment.create(null, comment)
      logger.debug("fbComment: {}", fbComment)
      logger.debug("CommentMessage : {} ", fbComment.getMessage)
    }
  }

  @Test
  @throws(classOf[Exception])
  def findGroups {
    val myGroups: Connection[Group] = dut.fetchConnection("/me/groups", classOf[Group], Parameter.`with`("limit", 10))
    logger.debug("group size : {}", myGroups.getData)
    import scala.collection.JavaConversions._
    for (groups <- myGroups) {
      import scala.collection.JavaConversions._
      for (group <- groups) {
        logger.debug(s"groupId : ${group.getId}, group name : ${group.getName}")
      }
    }
  }
}
