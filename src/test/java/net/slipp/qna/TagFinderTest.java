package net.slipp.qna;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

public class TagFinderTest {
	@Test
	public void parseTags() throws Exception {
		String tags = "java javascript";
		TagFinder dut = new TagFinder(tags);
		Set<Tag> parsedTags = dut.parseTags();
		assertThat(parsedTags.size(), is(2));
	}
}
