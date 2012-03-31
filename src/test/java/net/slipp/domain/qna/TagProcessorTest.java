package net.slipp.domain.qna;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Set;

import net.slipp.domain.qna.TagProcessor;

import org.junit.Before;
import org.junit.Test;

public class TagProcessorTest {
	private TagProcessor dut;
	
	@Before
	public void setup() {
		dut = new TagProcessor();
	}
	
	@Test
	public void parseTags() throws Exception {
		String plainTags = "java javascript";
		Set<String> parsedTags = dut.parseTags(plainTags);
		assertThat(parsedTags.size(), is(2));
	}
}
