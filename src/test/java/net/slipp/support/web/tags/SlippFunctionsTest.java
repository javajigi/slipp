package net.slipp.support.web.tags;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import net.slipp.domain.user.SocialUser;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlippFunctionsTest {
	private static final Logger logger = LoggerFactory.getLogger(SlippFunctionsTest.class);

	@Test
	public void isWriter() {
		boolean actual = SlippFunctions.isWriter(new SocialUser(10), new SocialUser(10));
		assertThat(actual, is(true));
	}
	
	@Test
	public void isNotWriter() throws Exception {
		boolean actual = SlippFunctions.isWriter(new SocialUser(10), new SocialUser(11));
		assertThat(actual, is(false));
		
		actual = SlippFunctions.isWriter(null, new SocialUser(11));
        assertThat(actual, is(false));
	}

	@Test
	public void wiki() throws Exception {
		String source = "{code:title=java}\n WikiContents wikiContents = new WikiContents();{code}\n" 
				+ "!1234!\n !2345!";
		String actual = SlippFunctions.wiki(source);
		logger.debug("convert wiki contents : {}", actual);
	}
	
	@Test
	public void stripHttp() throws Exception {
		String url = "http://localhost:8080";
		String actual = SlippFunctions.stripHttp(url);
		assertThat(actual, is("//localhost:8080"));
	}

	@Test
	public void links() throws Exception {
		String message = "길기용 이가 이렇게 정리했었던 기억이... ^^\n" +
			"http://www.slipp.net/wiki/display/SLS/mustache#mustache-잉여내용:Spring에서Handlebars를쓴다면?\n" +
			"이항희 님의 http://blog.javarouka.me/2014/08/handlebars-for-java_31.html";
		String actual = SlippFunctions.links(message);
		logger.debug("links message : {}", actual);
	}
}
