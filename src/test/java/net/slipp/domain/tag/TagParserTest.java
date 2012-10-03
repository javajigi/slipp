package net.slipp.domain.tag;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Set;

import net.slipp.repository.tag.TagRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Sets;

@RunWith(MockitoJUnitRunner.class)
public class TagParserTest {
	private TagParser dut;
	
	@Mock
	private TagRepository tagRepository;
	
	@Before
	public void setup() {
		dut = new TagParser(tagRepository);
	}
	
	@Test
	public void parseTags() throws Exception {
		String plainTags = "java javascript";
		Set<String> parsedTags = dut.parseTags(plainTags);
		assertThat(parsedTags.size(), is(2));
	}
	
	@Test
	public void processTags() throws Exception {
		Tag tag = new Tag("java");
		when(tagRepository.findByName(tag.getName())).thenReturn(tag);
		
		String plainTags = "java newTags";
		dut.processTags(plainTags);
		
		Set<Tag> pooledTags = dut.getPooledTags();
		assertThat(pooledTags.size(), is(1));
		Set<NewTag> newTags = dut.getNewTags();
		assertThat(newTags.size(), is(1));
	}
	
	@Test
	public void tagsToDenormalizedTags() throws Exception {
		Set<Tag> tags = Sets.newHashSet(new Tag("java"), new Tag("eclipse"));
		String result = TagParser.tagsToDenormalizedTags(tags);
		assertThat(result, is("java,eclipse"));
	}
}
