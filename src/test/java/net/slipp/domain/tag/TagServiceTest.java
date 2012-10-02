package net.slipp.domain.tag;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Set;

import net.slipp.repository.tag.NewTagRepository;
import net.slipp.repository.tag.TagRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TagServiceTest {
	private TagService dut;
	@Mock
	private TagRepository tagRepository;
	@Mock
	private NewTagRepository newTagRepository;
	
	@Before
	public void setup() {
		dut = new TagService(tagRepository, newTagRepository);
	}
	
	@Test
	public void parseTags() throws Exception {
		String plainTags = "java javascript";
		Set<String> parsedTags = dut.parseTags(plainTags);
		assertThat(parsedTags.size(), is(2));
	}
	
	@Test
	public void processTags_nonExistedNewTag() throws Exception {
		Tag tag = new Tag("java");
		when(tagRepository.findByName(tag.getName())).thenReturn(tag);
		
		String plainTags = "java newTags";
		Set<Tag> pooledTags = dut.processTags(plainTags);
		assertThat(pooledTags.size(), is(1));
		
		verify(newTagRepository).save(new NewTag("newTags"));
	}
	
	@Test
	public void processTags_existedNewTag() throws Exception {
		Tag tag = new Tag("java");
		NewTag newTag = new NewTag("newTags");
		when(tagRepository.findByName(tag.getName())).thenReturn(tag);
		when(newTagRepository.findByName("newTags")).thenReturn(newTag);
		
		String plainTags = "java " + newTag.getName();
		Set<Tag> pooledTags = dut.processTags(plainTags);
		assertThat(pooledTags.size(), is(1));
		
		assertThat(newTag.getTaggedCount(), is(2));
		verify(newTagRepository).save(newTag);
	}
	
	@Test
	public void moveToTagPool_부모_태그_ID_is_null() throws Exception {
		Long tagId = 1L;
		Long parentTagId = null;
		NewTag newTag = new NewTag(tagId, "newTag");
		when(newTagRepository.findOne(tagId)).thenReturn(newTag);
		
		dut.moveToTagPool(tagId, parentTagId);
		
		verify(tagRepository).save(newTag.createTag(null));
		verify(newTagRepository).delete(newTag);
	}
	
	@Test
	public void moveToTagPool_부모_태그가_존재함() throws Exception {
		Long tagId = 1L;
		Long parentTagId = 5L;
		NewTag newTag = new NewTag(tagId, "newTag");
		Tag parentTag = new Tag("새로운태그");
		when(tagRepository.findOne(parentTagId)).thenReturn(parentTag);
		when(newTagRepository.findOne(tagId)).thenReturn(newTag);
		
		dut.moveToTagPool(tagId, parentTagId);
		
		verify(tagRepository).save(newTag.createTag(parentTag));
		verify(newTagRepository).delete(newTag);
	}
}
