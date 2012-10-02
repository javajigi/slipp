package net.slipp.domain.tag;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
