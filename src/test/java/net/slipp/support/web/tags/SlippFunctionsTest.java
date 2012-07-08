package net.slipp.support.web.tags;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import net.slipp.domain.user.SocialUser;

import org.junit.Test;

public class SlippFunctionsTest {

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

}
