package net.slipp.web.notification

import java.util.List
import javax.annotation.Resource

import net.slipp.domain.user.SocialUser
import net.slipp.service.qna.NotificationService
import net.slipp.support.web.argumentresolver.LoginUser
import net.slipp.web.QnAPageableHelper._
import org.springframework.web.bind.annotation.{RequestMapping, RestController}

import scala.collection.JavaConversions._

@RestController
@RequestMapping(Array("/notifications"))
class NotificationController(@Resource(name = "notificationService") notificationService: NotificationService) {
  private val DefaultPageNo = 1
  private val DefaultPageSize = 5

  @RequestMapping(Array(""))
  def list(@LoginUser notifiee: SocialUser): List[NotificationForm] = {
    val questions = notificationService.findNotificationsAndReaded(notifiee)
    questions.map(q => {
      new NotificationForm(q.getQuestionId, q.getTitle, true)
    })
  }
  
  def this() = this(null)
}