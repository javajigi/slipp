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
	}

	@Test
	public void wiki() throws Exception {
		String source = "{code:title=java}\n WikiContents wikiContents = new WikiContents();{code}\n" 
				+ "!1234!\n !2345!";
		String actual = SlippFunctions.wiki(source, "http://localhost:8080");
		logger.debug("convert wiki contents : {}", actual);
	}
}
