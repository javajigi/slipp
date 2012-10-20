package net.slipp.domain.qna;

import java.util.List;
import java.util.Set;

import net.slipp.domain.user.SocialUser;

import com.google.common.collect.Sets;

public class Answers {
	private List<Answer> answers;

	public Answers(List<Answer> answers) {
		this.answers = answers;
	}

	public Set<SocialUser> findFacebookAnswerers() {
		Set<SocialUser> answerers = Sets.newHashSet();
		for (Answer answer : answers) {
			if (answer.isFacebookWriter()) {
				answerers.add(answer.getWriter());
			}
		}
		
		return answerers;
	}
}
