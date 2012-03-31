package net.slipp.qna.domain;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import net.slipp.qna.repository.MockTagRepository;

import org.junit.Before;
import org.junit.Test;

public class QuestionTest {
	private Question dut;
	private MockTagRepository tagRepository;
	
	@Before
	public void setup() {
		dut = new Question();
		tagRepository = new MockTagRepository();
	}
	
	@Test
	public void newQuestion() throws Exception {
		Question dut = new QuestionBuilder().tags("java javascript").build();
		dut.initializeTags(tagRepository);
		assertThat(dut.getTags().size(), is(2));
		assertThat(tagRepository.findByName("java").getTaggedCount(), is(1));
		assertThat(tagRepository.findByName("javascript").getTaggedCount(), is(1));
	}
	
	@Test
	public void updateQuestion() throws Exception {
		Question dut = new QuestionBuilder().tags("java javascript").build();
		dut.initializeTags(tagRepository);
		dut.setPlainTags("maven java eclipse");
		dut.initializeTags(tagRepository);
		assertThat(dut.getTags().size(), is(3));
		
		assertThat(tagRepository.findByName("maven").getTaggedCount(), is(1));
		assertThat(tagRepository.findByName("java").getTaggedCount(), is(1));
		assertThat(tagRepository.findByName("eclipse").getTaggedCount(), is(1));
		assertThat(tagRepository.findByName("javascript").getTaggedCount(), is(0));
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
	public void contents() throws Exception {
		String contents = "this is contents";
		dut.setContents(contents);
		assertThat(dut.getContents(), is(contents));
	}
}
