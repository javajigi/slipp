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
	public void processTags_최초_생성() throws Exception {
		// given
		Tag tag = new Tag("java");
		String plainTags = "java newTags";
		Set<Tag> originalTags = Sets.newHashSet();
		when(tagRepository.findByName(tag.getName())).thenReturn(tag);
		
		// when
		dut.processTags(originalTags, plainTags);
		
		// then
		Set<Tag> tags = dut.getTags();
		assertThat(tags.size(), is(1));
		assertThat(tag.getTaggedCount(), is(1));
		assertThat(dut.getDenormalizedTags(), is("java"));
		Set<NewTag> newTags = dut.getNewTags();
		assertThat(newTags.size(), is(1));
	}
	
	@Test
	public void processTags_수정() throws Exception {
		// given
		Tag java = createTaggedTag("java");
		Tag maven = createTaggedTag("maven");
		Tag spring = createTaggedTag("spring");
		String plainTags = "java maven newTags";
		Set<Tag> originalTags = Sets.newHashSet(java, maven, spring);
		when(tagRepository.findByName(java.getName())).thenReturn(java);
		when(tagRepository.findByName(maven.getName())).thenReturn(maven);
				
		// when
		dut.processTags(originalTags, plainTags);
		
		// then
		Set<Tag> tags = dut.getTags();
		assertThat(tags.size(), is(2));
		assertThat(java.getTaggedCount(), is(1));
		assertThat(maven.getTaggedCount(), is(1));
		assertThat(spring.getTaggedCount(), is(0));
		assertThat(dut.getDenormalizedTags(), is("java,maven"));
		Set<NewTag> newTags = dut.getNewTags();
		assertThat(newTags.size(), is(1));
	}

	private Tag createTaggedTag(String tagName) {
		Tag tag = new Tag(tagName);
		tag.tagged();
		return tag;
	}
	
	@Test
	public void parseTags() throws Exception {
		String plainTags = "java javascript";
		Set<String> parsedTags = TagParser.parseTags(plainTags);
		assertThat(parsedTags.size(), is(2));
	}
	
	@Test
	public void tagsToDenormalizedTags() throws Exception {
		Set<Tag> tags = Sets.newHashSet(new Tag("java"), new Tag("eclipse"));
		String result = TagParser.tagsToDenormalizedTags(tags);
		assertThat(result, is("java,eclipse"));
	}
}
