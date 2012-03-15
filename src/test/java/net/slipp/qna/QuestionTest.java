package net.slipp.qna;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import net.slipp.social.connect.SocialUser;

import org.junit.Before;
import org.junit.Test;

public class QuestionTest {
	private Question dut;
	
	@Before
	public void setup() {
		dut = new Question();
	}
	
	@Test
	public void newQuestion() throws Exception {
		SocialUser writer = new SocialUser();
		String title = "title";
		String contents = "contents";
		dut = Question.create(writer, title, contents);
		Tag tag = new Tag();
		dut.addTag(tag);
	}
	
	@Test
	public void addAnswer() {
		Answer answer = new Answer();
		dut.addAnswer(answer);
		assertThat(dut.getAnswers().size(), is(1));
		assertThat(dut.getAnswerCount(), is(1));
		
		assertThat(answer.getQuestion(), is(dut));
	}
	
	@Test
	public void addTag() throws Exception {
		Tag tag = new Tag();
		dut.addTag(tag);
		assertThat(dut.getTags().size(), is(1));
		assertThat(tag.getTaggedCount(), is(1));
	}
	
	@Test
	public void contents() throws Exception {
		String contents = "this is contents";
		dut.setContents(contents);
		assertThat(dut.getContents(), is(contents));
	}
}
