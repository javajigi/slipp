package net.slipp.domain.tag;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Set;

import net.slipp.domain.qna.Question;
import net.slipp.domain.user.SocialUser;

import org.junit.Test;

import com.google.common.collect.Iterables;

public class NewTagTest {

	@Test
	public void moveToTag() {
		// given
		String tagName = "newTag";
		NewTag newTag = createNewTag(tagName);
		Tag tag = newTag.createTag(null);
		
		// when
		newTag.moveToTag(tag);
		
		// then
		assertThat(newTag.isDeleted(), is(true));
		Question question = findFirstQuestion(newTag);
		assertThat(question.hasTag(tag), is(true));
	}

	private Question findFirstQuestion(NewTag newTag) {
		Set<Question> questions = newTag.getQuestions();
		return Iterables.getOnlyElement(questions);
	}

	private NewTag createNewTag(String tagName) {
		SocialUser loginUser = new SocialUser();
		Question question = new Question();
		NewTag newTag = new NewTag(tagName);
		newTag.tagged(loginUser, question);
		return newTag;
	}
}
