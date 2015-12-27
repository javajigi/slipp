package net.slipp.service.qna

import net.slipp.repository.notification.NotificationRepository
import net.slipp.repository.qna.AnswerRepository
import net.slipp.support.test.MockitoIntegrationTest
import org.junit.Test
import org.mockito.Mockito.when
import org.mockito.{InjectMocks, Mock}
import org.slf4j.LoggerFactory

class NotificationServiceTest extends MockitoIntegrationTest {
  private val logger = LoggerFactory.getLogger(classOf[NotificationServiceTest])
  @Mock private var notificationRepository: NotificationRepository = _
  @Mock private var answerRepository: AnswerRepository = _
  @InjectMocks private val dut: NotificationService = new NotificationService

  @Test def countByNotifiee() {
    val someUser = aSomeUser()
    when(notificationRepository.countByNotifiee(someUser)).thenReturn(2L)
    val count = dut.countByNotifiee(someUser)
    logger.debug(s"count is ${count}")
  }
}