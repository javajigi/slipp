package net.slipp.service.qna;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import net.slipp.domain.notification.Notification;
import net.slipp.domain.qna.Question;
import net.slipp.domain.user.SocialUser;
import net.slipp.repository.notification.NotificationRepository;
import net.slipp.support.http.HttpClientManager;
import net.slipp.support.http.HttpInvocationSupport;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@Transactional
public class NotificationService {

	@Value("#{applicationProperties['facebook.accessToken']}")
	private String accessToken;

	@Resource(name = "notificationRepository")
	private NotificationRepository notificationRepository;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Async
	public void notifyToFacebook(SocialUser loginUser, Question question, Set<SocialUser> notifieeUsers) {
		if (notifieeUsers.isEmpty()) {
			return;
		}

		for (SocialUser notifiee : notifieeUsers) {
			String uri = String.format("/%s/notifications", notifiee.getProviderUserId());
			HttpInvocationSupport invocation = new HttpInvocationSupport(uri) {
				@Override
				protected Object parseResponseBody(String body) throws Exception {
					return null;
				}
			};
			invocation.addParameter("access_token", accessToken);
			invocation.addParameter("template",
					String.format("%s님이 \"%s\" 글에 답변을 달았습니다.", loginUser.getUserId(), question.getTitle()));
			invocation.addParameter("href", String.format("/questions/%d", question.getQuestionId()));

			HttpClientManager manager = new HttpClientManager();
			manager.post("https://graph.facebook.com", invocation);
		}
	}

	@Async
	public void notifyToSlipp(SocialUser notifier, Question question, Set<SocialUser> notifieeUsers) {
		Assert.notNull(notifier, "LoginUser should be not null!");
		Assert.notNull(question, "Question should be not null!");
		
		if (notifieeUsers.isEmpty()) {
			return;
		}
		
		for (SocialUser notifiee : notifieeUsers) {
			Notification notification = new Notification(notifier, notifiee, question);
			notificationRepository.save(notification);
		}
	}
	
	public long countByNotifiee(SocialUser notifiee) {
	    Long count = notificationRepository.countByNotifiee(notifiee);
	    if (count == null) {
	        return 0L;
	    }
	    return count;	    
	}
	
	public List<Notification> findNotificationsAndReaded(SocialUser notifiee, Pageable pageable) {
	    List<Notification> notifications = notificationRepository.findNotifications(notifiee, pageable);
	    notificationRepository.updateReaded(notifiee);
	    return notifications;
	}
}
