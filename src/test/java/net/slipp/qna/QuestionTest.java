package net.slipp.qna;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import net.slipp.qna.repository.MockTagRepository;
import net.slipp.qna.repository.TagRepository;

import org.junit.Before;
import org.junit.Test;

public class QuestionTest {
	private Question dut;
	private TagRepository tagRepository;
	
	@Before
	public void setup() {
		dut = new Question();
		tagRepository = new MockTagRepository();
	}
	
	@Test
	public void newQuestion_to() throws Exception {
		Question dut = new QuestionBuilder().tags("java javascript").build();
		dut.parseAndLoadTags(tagRepository);
		assertThat(dut.getTags().size(), is(2));
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
		Tag tag = new Tag("java");
		dut.taggedBy(tag);
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
