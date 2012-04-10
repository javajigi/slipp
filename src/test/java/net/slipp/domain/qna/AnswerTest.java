package net.slipp.domain.qna;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class AnswerTest {
	private Answer dut;
	
	@Before
	public void setup() {
		dut = new Answer();
	}
	
	@Test
	public void answerToQuestion() throws Exception {
		Question question = new Question();
		dut.answerTo(question);
		assertThat(question.getAnswerCount(), is(1));
	}
}
