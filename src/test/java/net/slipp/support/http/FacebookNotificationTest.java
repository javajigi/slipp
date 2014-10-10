package net.slipp.support.http;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationContext.xml")
public class FacebookNotificationTest {
	@Value("#{applicationProperties['facebook.accessToken']}")
	private String accessToken;
	
	@Test
	public void notification() throws Exception {
		@SuppressWarnings("rawtypes")
		HttpInvocationSupport invocation = new HttpInvocationSupport("/1324855987/notifications") {
			@Override
			protected Object parseResponseBody(String body) throws Exception {
				System.out.println(body);
				return null;
			}
		};
		invocation.addParameter("access_token", accessToken);
		invocation.addParameter("template", "자바지기님이 \"github에 새로운 브랜치를 생성하는 방법은?\"에 답변을 달았습니다.");
		invocation.addParameter("href", "/questions/1");
		
//		HttpClientManager manager = new HttpClientManager();
//		manager.post("https://graph.facebook.com", invocation);
	}
}
