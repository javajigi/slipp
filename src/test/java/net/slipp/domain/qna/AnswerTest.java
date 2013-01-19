package net.slipp.domain.qna;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class AnswerTest {
	@Test
	public void likedMoreThan() {
		Answer answer = createAnswerWithSumLike(2);
		assertThat(answer.likedMoreThan(2), is(true));
	}

	static Answer createAnswerWithSumLike(final int sumLike) {
		Answer answer = new Answer() {
			@Override
			public Integer getSumLike() {
				return sumLike;
			}
		};
		return answer;
	}
	
	@Test
	public void isNotBest() throws Exception {
		Answer answer = createAnswerWithSumLike(1);
		assertThat(answer.likedMoreThan(2), is(false));		
	}
}
