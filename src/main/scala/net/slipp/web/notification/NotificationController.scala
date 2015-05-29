package net.slipp.web.notification

import java.util.List

import javax.annotation.Resource

import scala.collection.JavaConversions._

import net.slipp.domain.notification.Notification
import net.slipp.domain.qna.Question
import net.slipp.domain.user.SocialUser
import net.slipp.service.qna.NotificationService
import net.slipp.support.web.argumentresolver.LoginUser
import net.slipp.web.QnAPageableHelper._

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping(Array("/notifications"))
class NotificationController(@Resource(name = "notificationService") notificationService: NotificationService) {
  private val DefaultPageNo = 1
  private val DefaultPageSize = 5

  @RequestMapping(Array(""))
  @ResponseBody def list(@LoginUser notifiee: SocialUser): List[NotificationForm] = {
    val pageable = createPageable(DefaultPageNo, DefaultPageSize, "notificationId")
    val notifications = notificationService.findNotificationsAndReaded(notifiee, pageable)
    notifications.toList.map ( notification => {
      val question = notification.getQuestion()
      new NotificationForm(question.getQuestionId(), question.getTitle(), notification.isReaded())
    })
  }
  
  def this() = this(null)
}