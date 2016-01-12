package net.slipp.service.qna

import java.util.{List, Set}
import javax.annotation.Resource

import com.restfb.types.FacebookType
import com.restfb.{DefaultFacebookClient, FacebookClient, Parameter, Version}
import net.slipp.domain.notification.Notification
import net.slipp.domain.qna.{Answer, Question}
import net.slipp.domain.user.SocialUser
import net.slipp.repository.notification.NotificationRepository
import net.slipp.repository.qna.AnswerRepository
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.Assert

import scala.collection.JavaConversions._

@Service
@Transactional class NotificationService {
  private var logger: Logger = LoggerFactory.getLogger(classOf[NotificationService])

  @Autowired private var env: Environment = null
  @Resource(name = "answerRepository") private var answerRepository: AnswerRepository = null
  @Resource(name = "notificationRepository") private var notificationRepository: NotificationRepository = null

  @Async def notifyToFacebook(loginUser: SocialUser, answerId: Long) {
    Assert.notNull(answerId, "answerId should be not null!")
    val answer: Answer = answerRepository.findOne(answerId)
    Assert.notNull(answer, "Answer should be not null!")
    val question: Question = answer.getQuestion
    val notifieeUsers: Set[SocialUser] = question.findNotificationUser(loginUser)
    logger.debug(s"Notification Users Size : ${notifieeUsers.size()}")
    val facebookClient: FacebookClient = new DefaultFacebookClient(createAccessToken, Version.VERSION_2_2)
    for (notifiee <- notifieeUsers) {
      val uri = s"/${notifiee.getProviderUserId}/notifications"
      val template = s"""${loginUser.getUserId}님이 \"${question.getTitle}\" 글에 답변을 달았습니다."""
      val href = s"/questions/${question.getQuestionId}#answer-${answer.getAnswerId}"
      facebookClient.publish(uri, classOf[FacebookType], Parameter.`with`("template", template), Parameter.`with`("href", href))
    }
  }

  private def createAccessToken: String = {
    val accessToken: FacebookClient.AccessToken = new DefaultFacebookClient(Version.VERSION_2_2).obtainAppAccessToken(env.getProperty("facebook.clientId"), env.getProperty("facebook.clientSecret"))
    logger.debug("AccessToken : {}", accessToken)
    return accessToken.getAccessToken
  }

  @Async def notifyToSlipp(notifier: SocialUser, answerId: Long) {
    Assert.notNull(answerId, "answerId should be not null!")
    val answer: Answer = answerRepository.findOne(answerId)
    Assert.notNull(answer, "Answer should be not null!")
    val question: Question = answer.getQuestion

    question.findNotificationUser(notifier).foreach(notifiee => {
      val notification: Notification = new Notification(notifier, notifiee, question)
      notificationRepository.save(notification)
    })
  }

  def countByNotifiee(notifiee: SocialUser) = {
    notificationRepository.countByNotifiee(notifiee)
  }

  def findNotificationsAndReaded(notifiee: SocialUser): List[Question] = {
    val questions: List[Question] = notificationRepository.findQuestionsFromNotification(notifiee)
    notificationRepository.updateReaded(notifiee)
    questions
  }
}
