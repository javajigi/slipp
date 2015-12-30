package net.slipp.domain.notification

import javax.persistence.{Column, Entity, ForeignKey, GeneratedValue, GenerationType, Id, JoinColumn, OneToOne}

import net.slipp.domain.qna.Question
import net.slipp.domain.user.SocialUser
import net.slipp.support.jpa.DomainModel

@Entity
class Notification(nfier: SocialUser, nfiee: SocialUser, q: Question) extends DomainModel {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var notificationId: Long = _

  @OneToOne
  @JoinColumn(foreignKey = new ForeignKey(name = "fk_notification_notifier"))
  var notifier: SocialUser = nfier

  @OneToOne
  @JoinColumn(foreignKey = new ForeignKey(name = "fk_notification_notifiee"))
  var notifiee: SocialUser = nfiee

  @OneToOne
  @JoinColumn(foreignKey = new ForeignKey(name = "fk_notification_question"))
  var question: Question = q

  @Column(name = "readed", nullable = false)
  var readed: Boolean = false

  def getNotificationId: Long = {
    return notificationId
  }

  def getNotifier: SocialUser = {
    return notifier
  }

  def getNotifiee: SocialUser = {
    return notifiee
  }

  def getQuestion: Question = {
    return question
  }

  def isReaded: Boolean = {
    return readed
  }

  def this() = this(null, null, null)
}
