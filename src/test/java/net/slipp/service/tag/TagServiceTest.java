package net.slipp.service.tag;

import static net.slipp.domain.tag.TagBuilder.aTag;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Set;

import net.slipp.domain.tag.Tag;
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
	
	@Before
	public void setup() {
		dut = new TagService(tagRepository);
	}
	
	@Test
	public void moveToTag_부모_태그_ID_is_null() throws Exception {
		Long parentTagId = null;
		Tag newTag = aTag().withId(1L).withName("newTag").withPooled(false).build();
		when(tagRepository.findOne(parentTagId)).thenReturn(null);
		when(tagRepository.findOne(newTag.getTagId())).thenReturn(newTag);
		
		dut.moveToTag(newTag.getTagId(), parentTagId);
		
		assertThat(newTag.isPooled(), is(true));
	}
	
    @Test
    public void processTags_최초_생성() throws Exception {
        // given
        Tag java = Tag.pooledTag("java");
        Tag newTag = Tag.newTag("newTag");
        String plainTags = String.format("%s %s", java.getName(), newTag.getName());
        when(tagRepository.findByName(java.getName())).thenReturn(java);
        when(tagRepository.save(newTag)).thenReturn(newTag);
        
        // when
        Set<Tag> tags = dut.processTags(plainTags);
        
        // then
        assertThat(tags.contains(java), is(true));
        assertThat(tags.contains(newTag), is(true));
    }
    
    @Test
    public void parseTags_space() throws Exception {
        String plainTags = "java javascript";
        Set<String> parsedTags = TagService.parseTags(plainTags);
        assertThat(parsedTags.size(), is(2));
    }
    
    @Test
    public void parseTags_comma() throws Exception {
        String plainTags = "java,javascript";
        Set<String> parsedTags = TagService.parseTags(plainTags);
        assertThat(parsedTags.size(), is(2));
        
        plainTags = "java,javascript,";
        parsedTags = TagService.parseTags(plainTags);
        assertThat(parsedTags.size(), is(2));
    }
}
