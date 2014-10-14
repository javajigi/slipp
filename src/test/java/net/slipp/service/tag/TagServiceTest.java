package net.slipp.service.tag;

import static net.slipp.domain.tag.TagBuilder.aTag;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Set;

import net.slipp.domain.fb.FacebookGroup;
import net.slipp.domain.tag.Tag;
import net.slipp.repository.tag.TagRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Sets;

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
		Tag newTag = aTag().withId(1L).withName("newTag").build();
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
    public void processGroupTags() throws Exception {
        FacebookGroup groupTag1 = new FacebookGroup("1234", "생활코딩");
        FacebookGroup groupTag2 = new FacebookGroup("5678", "slipp");
        FacebookGroup groupTag3 = new FacebookGroup("9123", "Na우리Next");
        Tag tag1 = Tag.groupedTag(groupTag1.getName(), groupTag1.getGroupId());
        Tag tag2 = Tag.pooledTag(groupTag2.getName());
        Tag tag3 = Tag.groupedTag(groupTag3.getName(), groupTag3.getGroupId());
        
        Set<FacebookGroup> newTags = Sets.newHashSet(groupTag1, groupTag2, groupTag3);
        when(tagRepository.findByGroupId(groupTag1.getGroupId())).thenReturn(tag1);
        when(tagRepository.findByGroupId(groupTag2.getGroupId())).thenReturn(null);
        when(tagRepository.findByName(groupTag2.getName())).thenReturn(Tag.pooledTag(groupTag2.getName()));
        when(tagRepository.findByGroupId(groupTag3.getGroupId())).thenReturn(null);
        when(tagRepository.save(tag3)).thenReturn(tag3);
        
        // when
        Set<Tag> tags = dut.processGroupTags(newTags);
        
        // then
        assertThat(tags.contains(tag1), is(true));
        assertThat(tags.contains(tag2), is(true));
        assertThat(tags.contains(tag3), is(true));
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
