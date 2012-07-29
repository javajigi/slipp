package net.slipp.domain.qna;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;

import net.slipp.repository.qna.MockTagRepository;
import net.slipp.repository.qna.NewTagRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TagProcessorTest {
	private TagProcessor dut;
	private MockTagRepository tagRepository;
	@Mock
	private NewTagRepository newTagRepository;
	
	@Before
	public void setup() {
		tagRepository = new MockTagRepository();
		dut = new TagProcessor(tagRepository, newTagRepository);
	}
	
	@Test
	public void parseTags() throws Exception {
		String plainTags = "java javascript";
		Set<String> parsedTags = dut.parseTags(plainTags);
		assertThat(parsedTags.size(), is(2));
	}
	
	@Test
	public void processTags_nonExistedNewTag() throws Exception {
		String plainTags = "java newTags";
		Set<Tag> pooledTags = dut.processTags(plainTags);
		assertThat(pooledTags.size(), is(1));
		
		verify(newTagRepository).save(new NewTag("newTags"));
	}
	
	@Test
	public void processTags_existedNewTag() throws Exception {
		NewTag newTag = new NewTag("newTags");
		when(newTagRepository.findByName("newTags")).thenReturn(newTag);
		
		String plainTags = "java " + newTag.getName();
		Set<Tag> pooledTags = dut.processTags(plainTags);
		assertThat(pooledTags.size(), is(1));
		
		assertThat(newTag.getTaggedCount(), is(2));
		verify(newTagRepository).save(newTag);
	}
}
