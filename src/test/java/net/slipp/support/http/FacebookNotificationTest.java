package net.slipp.support.http;

import org.junit.Test;

public class FacebookNotificationTest {
	@Test
	public void notification() throws Exception {
		@SuppressWarnings("rawtypes")
		HttpInvocationSupport invocation = new HttpInvocationSupport("/{recipient_userid}/notifications?access_token= … &template= … &href= …") {
			@Override
			protected Object parseResponseBody(String body) throws Exception {
				return null;
			}
		};
		invocation.addParameter("access_token", "");
		invocation.addParameter("template", "댓글이 추가되었습니다.");
		invocation.addParameter("href", "http://www.slipp.net");
		
		HttpClientManager manager = new HttpClientManager();
		manager.post("", invocation);
		
	}
}
