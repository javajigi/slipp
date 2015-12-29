package net.slipp.service.qna

import com.restfb.{DefaultFacebookClient, FacebookClient, Parameter, Version}
import com.restfb.types.FacebookType
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.support.AnnotationConfigContextLoader
import slipp.config.TestConfig

@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(classes = Array(classOf[TestConfig]), loader = classOf[AnnotationConfigContextLoader])
class NotificationTest {
  private var logger: Logger = LoggerFactory.getLogger(classOf[NotificationTest])
  @Autowired private var env: Environment = null

  private def createAccessToken: FacebookClient.AccessToken = {
    val accessToken: FacebookClient.AccessToken = new DefaultFacebookClient(Version.VERSION_2_2).obtainAppAccessToken(env.getProperty("facebook.clientId"), env.getProperty("facebook.clientSecret"))
    logger.debug("AccessToken : {}", accessToken)
    return accessToken
  }

  @Test def notification {
    val accessToken: FacebookClient.AccessToken = createAccessToken
    val facebookClient: FacebookClient = new DefaultFacebookClient(accessToken.getAccessToken, Version.VERSION_2_0)
    val uri: String = String.format("/%s/notifications", "1324855987")
    val template: String = String.format("%s님이 \"%s\" 글에 답변을 달았습니다.", "javajigi", "this is title")
    val href: String = String.format("/questions/1#answer-5")
    val result: FacebookType = facebookClient.publish(uri, classOf[FacebookType], Parameter.`with`("template", template), Parameter.`with`("href", href))
    logger.debug("result : {}", result)
  }
}
