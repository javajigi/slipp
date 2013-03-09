package net.slipp.repository.notification;

import java.util.List;

import net.slipp.domain.notification.Notification;
import net.slipp.support.jpa.SlippCommonRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends SlippCommonRepository<Notification, Long>{
	@Query("select o from Notification o where o.targetSocialUserId = :socialUserId and readed = false group by o.postId")
	public List<Notification> findNotifications(@Param("socialUserId")Long socialUserId);
}
