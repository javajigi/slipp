package net.slipp.domain.qna;

import static net.slipp.domain.qna.AnswerBuilder.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class AnswerTest {
	@Test
	public void likedMoreThan() {
		Answer answer = anAnswer().withTotalLiked(2).build();
		assertThat(answer.likedMoreThan(2), is(true));
	}

	@Test
	public void isNotBest() throws Exception {
		Answer answer = anAnswer().withTotalLiked(1).build();
		assertThat(answer.likedMoreThan(2), is(false));		
	}
}
