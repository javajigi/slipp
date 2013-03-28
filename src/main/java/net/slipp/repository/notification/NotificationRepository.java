package net.slipp.repository.notification;

import java.util.List;

import net.slipp.domain.notification.Notification;
import net.slipp.domain.user.SocialUser;
import net.slipp.support.jpa.SlippCommonRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends SlippCommonRepository<Notification, Long>{
	@Query("select n from Notification n where n.notifiee = :notifiee group by n.question")
	List<Notification> findNotifications(@Param("notifiee") SocialUser notifiee, Pageable pageable);
	
	@Modifying
	@Query("UPDATE Notification n set n.readed = true where n.notifiee = :notifiee and n.readed = false")
	void updateReaded(@Param("notifiee") SocialUser notifiee);

	@Query("SELECT count(distinct n.question) from Notification n where n.notifiee = :notifiee and n.readed = false")
    Long countByNotifiee(@Param("notifiee") SocialUser notifiee);
}
