package net.slipp.repository.notification

import java.lang.Long
import java.util.List

import net.slipp.domain.notification.Notification
import net.slipp.domain.user.SocialUser
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.{JpaRepository, Modifying, Query}
import org.springframework.data.repository.query.Param

trait NotificationRepository extends JpaRepository[Notification, Long] {
  @Query("select n from Notification n where n.notifiee = :notifiee group by n.question")
  def findNotifications(@Param("notifiee") notifiee: SocialUser, pageable: Pageable): List[Notification]

  @Modifying
  @Query("UPDATE Notification n set n.readed = true where n.notifiee = :notifiee and n.readed = false")
  def updateReaded(@Param("notifiee") notifiee: SocialUser)

  @Query("SELECT count(distinct n.question) from Notification n where n.notifiee = :notifiee and n.readed = false")
  def countByNotifiee(@Param("notifiee") notifiee: SocialUser): Long
}
