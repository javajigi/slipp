package net.slipp.service.qna;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import net.slipp.domain.notification.Notification;
import net.slipp.domain.qna.Answer;
import net.slipp.domain.qna.Question;
import net.slipp.domain.user.SocialUser;
import net.slipp.repository.notification.NotificationRepository;
import net.slipp.support.http.HttpClientManager;
import net.slipp.support.http.HttpInvocationSupport;

import org.springframework.beans.factory.annotation.Value;
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
	public void notifyToFacebook(SocialUser loginUser, Question question, Set<SocialUser> notifierUsers) {
		if (notifierUsers.isEmpty()) {
			return;
		}

		for (SocialUser socialUser : notifierUsers) {
			String uri = String.format("/%s/notifications", socialUser.getProviderUserId());
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
	public void notifyToSlipp(Question question, Answer answer) {
		Assert.notNull(question, "Question should be not null!");
		Assert.notNull(answer, "Answer should be not null!");
		Assert.notNull(notificationRepository, "NotificationRepository should be not null!");
		
		if (!question.isWritedBy(answer.getWriter())) { // 본인 글의 경우는 알림이 필요없다.
			Notification persistNotification = Notification.create(question.getWriter().getId(), question.getQuestionId());
			notificationRepository.save(persistNotification);
		}
	}

	public List<Notification> findNotifications(SocialUser loginUser) {
		return notificationRepository.findNotifications(loginUser.getId());
	}
	
	public void readNotifications(SocialUser loginUser) {
		List<Notification> notifications = notificationRepository.findNotifications(loginUser.getId());
		for (Notification notification : notifications) {
			notification.read();
		}
	}
}
