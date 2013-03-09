package net.slipp.repository.notification;

import java.util.List;

import net.slipp.domain.notification.Notification;
import net.slipp.domain.user.SocialUser;
import net.slipp.support.jpa.SlippCommonRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends SlippCommonRepository<Notification, Long>{
	@Query("select o from Notification o where o.notifier = :notifier and readed = false group by o.question.questionId")
	public List<Notification> findNotifications(@Param("notifier")SocialUser notifier, Pageable pageable);
	
	@Query("select o from Notification o where o.notifier = :notifier and readed = false")
	public List<Notification> findAllNotifications(@Param("notifier")SocialUser notifier);
}
