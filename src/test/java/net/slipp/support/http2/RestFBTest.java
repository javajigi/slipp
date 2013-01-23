package net.slipp.support.http2;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;
import com.restfb.types.Post;

@Ignore
public class RestFBTest {
	private static Logger logger = LoggerFactory.getLogger(RestFBTest.class);
	
	private FacebookClient dut;

	@Before
	public void setup() {
		String accessToken = "access_token";
		dut = new DefaultFacebookClient(accessToken);
	}

	@Test
	public void post() throws Exception {
		String message = "글쓰기 테스트입니다.";
		FacebookType response = dut.publish("me/feed", FacebookType.class, 
			Parameter.with("message", message));
		String id = response.getId();
		logger.debug("id : {}", id);
		
		Post post = dut.fetchObject(id, Post.class);
		assertThat(post.getMessage(), is(message));
	}
}
