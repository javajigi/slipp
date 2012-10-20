package net.slipp.service.qna;

import java.util.Set;

import net.slipp.domain.qna.Question;
import net.slipp.domain.user.SocialUser;
import net.slipp.support.http.HttpClientManager;
import net.slipp.support.http.HttpInvocationSupport;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
	@Value("#{applicationProperties['facebook.accessToken']}")
	private String accessToken;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void notifyToFacebook(SocialUser loginUser, Question question) {
		Set<SocialUser> notifierUsers = question.findNotificationUser(loginUser);
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
			invocation.addParameter("template", String.format("{%s}이 %s에 답변을 했습니다.", loginUser.getProviderUserId(), question.getTitle()));
			invocation.addParameter("href", String.format("/questions/%d", question.getQuestionId()));
			
			HttpClientManager manager = new HttpClientManager();
			manager.post("https://graph.facebook.com", invocation);			
		}
	}
}
