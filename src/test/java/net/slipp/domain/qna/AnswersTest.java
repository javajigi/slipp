package net.slipp.domain.qna;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Set;

import net.slipp.domain.user.SocialUser;

import org.junit.Test;

import com.google.common.collect.Lists;

public class AnswersTest {

	@Test
	public void findFacebookAnswerer() {
		Answer answer1 = new Answer();
		answer1.writedBy(createSocialUser(1L, "facebook"));
		Answer answer2 = new Answer();
		answer2.writedBy(createSocialUser(2L, "google"));
		
		List<Answer> persistedAnswers = Lists.newArrayList(answer1, answer2);
		Answers answers = new Answers(persistedAnswers);
		Set<SocialUser> answerers = answers.findFacebookAnswerers();
		assertThat(answerers.size(), is(1));
	}
	
	@Test
	public void isBestAnswer() throws Exception {
		Answer answer = new Answer();
		assertThat(answer.isBest(), is(false));
		
		answer.setSumLike(2);
		assertThat(answer.isBest(), is(true));
	}
	
	private SocialUser createSocialUser(long id, String providerId) {
		SocialUser socialUser = new SocialUser(id);
		socialUser.setProviderId(providerId);
		return socialUser;
	}
}
