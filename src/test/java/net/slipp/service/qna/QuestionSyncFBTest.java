package net.slipp.service.qna;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.Comment;
import com.restfb.types.FacebookType;
import com.restfb.types.Post;
import com.restfb.types.Post.Comments;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationContext.xml")
public class QuestionSyncFBTest {
	private static Logger log = LoggerFactory.getLogger(QuestionSyncFBTest.class);
	
	@Value("#{applicationProperties['my.facebook.accessToken']}")
	private String myAccessToken;
	
	private FacebookClient dut;

	@Before
	public void setup() {
		dut = new DefaultFacebookClient(myAccessToken, Version.VERSION_2_2);
	}
	
	@Test
	public void post() throws Exception {
		String message = "글쓰기 테스트입니다.";
		FacebookType response = dut.publish("me/feed", FacebookType.class, 
		    Parameter.with("link", "http://www.slipp.net/questions/87"),
			Parameter.with("message", message));
		String id = response.getId();
		log.debug("id : {}", id);
		
		Post post = dut.fetchObject(id, Post.class);
		assertThat(post.getMessage(), is(message));
	}
	
	@Test
	public void findComments() throws Exception {
		Post post = dut.fetchObject("1324855987_4834614866310", Post.class);
		Comments comments = post.getComments();
		for (Comment comment : comments.getData()) {
			log.debug("comment : {}", comment);
		}
	}
}
