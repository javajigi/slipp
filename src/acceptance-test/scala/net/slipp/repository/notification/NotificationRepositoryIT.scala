package net.slipp.repository.notification

import java.util.{HashSet, List}

import net.slipp.domain.notification.Notification
import net.slipp.domain.qna.Question
import net.slipp.domain.tag.Tag
import net.slipp.domain.user.{SocialUser, SocialUserBuilder}
import net.slipp.repository.qna.QuestionRepository
import net.slipp.repository.user.SocialUserRepository
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.domain.{PageRequest, Pageable, Sort}
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StopWatch
import slipp.config.PersistenceJPAConfig

@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(classes = Array(classOf[PersistenceJPAConfig]))
class NotificationRepositoryIT {
  private val log: Logger = LoggerFactory.getLogger(classOf[NotificationRepositoryIT])

  @Autowired private var notificationRepository: NotificationRepository = null
  @Autowired private var questionRepository: QuestionRepository = null
  @Autowired private var socialUserRepository: SocialUserRepository = null
  private var stopWatch: StopWatch = new StopWatch

  @Test @Transactional def notification_lifecycle {
    var notifiee: SocialUser = SocialUserBuilder.aSocialUser.createTestUser("javajigi")
    notifiee = socialUserRepository.save(notifiee)
    var notifier: SocialUser = SocialUserBuilder.aSocialUser.createTestUser("sanjigi")
    notifier = socialUserRepository.save(notifier)
    var question: Question = new Question(notifiee, "this is title", "this is contents", new HashSet[Tag])
    question = questionRepository.save(question)
    val notification: Notification = new Notification(notifier, notifiee, question)
    notificationRepository.save(notification)
    val pageable: Pageable = new PageRequest(0, 5, new Sort(Direction.DESC, "notificationId"))

    stopWatch.start
    var questions: List[Question] = notificationRepository.findQuestionsFromNotification(notifiee)
    assertThat(questions.size, is(1))
    stopWatch.stop
    log.debug("time to create: {}", stopWatch.getLastTaskTimeMillis + "ms")
    var count: Long = notificationRepository.countByNotifiee(notifiee)
    assertThat(count, is(1L))
    notificationRepository.updateReaded(notifiee)
    count = notificationRepository.countByNotifiee(notifiee)
    assertThat(count, is(0L))
  }
}
