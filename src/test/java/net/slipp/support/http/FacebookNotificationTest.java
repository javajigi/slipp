package net.slipp.support.http;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationContext.xml")
public class FacebookNotificationTest {
	@Value("#{applicationProperties['facebook.accessToken']}")
	private String accessToken;
	
	@SuppressWarnings("unchecked")
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
		invocation.addParameter("template", "{1324855987} comment");
		invocation.addParameter("href", "/questions/1");
		
		HttpClientManager manager = new HttpClientManager();
		manager.post("https://graph.facebook.com", invocation);
	}
}
