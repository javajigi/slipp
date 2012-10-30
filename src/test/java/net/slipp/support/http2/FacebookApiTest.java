package net.slipp.support.http2;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FacebookApiTest {
	private static Logger logger = LoggerFactory.getLogger(FacebookApiTest.class);
	
	private HttpClientTemplate dut;
	
	@Before
	public void before() throws Exception {
		dut = new HttpClientTemplate("https://graph.facebook.com");
	}
	
	@Test
	public void readFeed() throws Exception {
		HttpInvocation httpInvocation = new HttpInvocationSupport("/me/feed", "UTF-8") {
			@Override
			protected Object parseResponseBody(String body) throws Exception {
				logger.debug("body : {}", body);
				return body;
			}
		};
		httpInvocation.addParameter("access_token", "access_token");
		dut.get(httpInvocation);
	}
	
	@Test
	public void post() throws Exception {
		HttpInvocation httpInvocation = new HttpInvocationSupport("/me/feed", "UTF-8") {
			@Override
			protected Object parseResponseBody(String body) throws Exception {
				logger.debug("body : {}", body);
				return body;
			}
		};
		
		httpInvocation.addParameter("access_token", "access_token");
		httpInvocation.addParameter("message", "글쓰기 테스트입니다.");
		dut.post(httpInvocation);
	}
}
