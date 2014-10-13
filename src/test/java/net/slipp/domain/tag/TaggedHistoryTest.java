package net.slipp.domain.tag;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class TaggedHistoryTest {

	@Test
	public void create() {
		Long tagId = 1L;
		Long questionId = 2L;
		TaggedHistory actual = new TaggedHistory(tagId, questionId);
		assertThat(actual, is(new TaggedHistory(tagId, questionId)));
	}

}
