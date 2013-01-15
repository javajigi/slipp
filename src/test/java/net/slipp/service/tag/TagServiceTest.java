package net.slipp.service.tag;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;

import net.slipp.domain.qna.Question;
import net.slipp.domain.tag.NewTag;
import net.slipp.domain.tag.Tag;
import net.slipp.domain.tag.Tags;
import net.slipp.domain.user.SocialUser;
import net.slipp.repository.tag.NewTagRepository;
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
	
	@Mock
	private NewTagRepository newTagRepository;
	
	@Before
	public void setup() {
		dut = new TagService(tagRepository, newTagRepository);
	}
	
	@Test
	public void moveToTag_부모_태그_ID_is_null() throws Exception {
		NewTag newTag = new NewTag(1L, "java"); 
		Long parentTagId = null;
		when(newTagRepository.findOne(newTag.getTagId())).thenReturn(newTag);
		when(tagRepository.findOne(parentTagId)).thenReturn(null);
		when(tagRepository.findByName(newTag.getName())).thenReturn(null);
		Tag tag = newTag.createTag(null);
		when(tagRepository.save(tag)).thenReturn(tag);
		
		dut.moveToTag(newTag.getTagId(), parentTagId);
		
		assertThat(newTag.isDeleted(), is(true));
	}
	
	@Test
	public void moveToTag_부모_태그가_존재함() throws Exception {
		Long tagId = 1L;
		Long parentTagId = 5L;
		NewTag newTag = new NewTag(tagId, "newTag");
		Tag parentTag = new Tag("새로운태그");
		when(tagRepository.findOne(parentTagId)).thenReturn(parentTag);
		when(newTagRepository.findOne(tagId)).thenReturn(newTag);
		
		dut.moveToTag(tagId, parentTagId);
		
		verify(tagRepository).save(newTag.createTag(parentTag));
		assertThat(newTag.isDeleted(), is(true));
	}
	
	@Test
	public void saveNewTag_최초_신규_태그() throws Exception {
		SocialUser loginUser = new SocialUser();
		Question question = new Question();
		NewTag newTag = new NewTag("newTag");
		Set<NewTag> newTags = Sets.newHashSet(newTag);

		when(newTagRepository.findByName(newTag.getName())).thenReturn(null);
		
		dut.saveNewTag(loginUser, question, newTags);
		
		verify(newTagRepository).save(newTag);
	}
	
	@Test
	public void saveNewTag_이미_존재하는_태그() throws Exception {
		SocialUser loginUser = new SocialUser();
		Question question = new Question();
		NewTag newTag = new NewTag("newTag");
		Set<NewTag> newTags = Sets.newHashSet(newTag);

		when(newTagRepository.findByName(newTag.getName())).thenReturn(newTag);
		
		dut.saveNewTag(loginUser, question, newTags);
		
		verify(newTagRepository, never()).save(newTag);
	}
	
    @Test
    public void processTags_최초_생성() throws Exception {
        // given
        Tag tag = new Tag("java");
        String plainTags = "java newTags";
        Set<Tag> originalTags = Sets.newHashSet();
        when(tagRepository.findByName(tag.getName())).thenReturn(tag);
        
        // when
        Tags tags = dut.processTags(originalTags, plainTags);
        
        // then
        Set<Tag> pooledTags = tags.getPooledTags();
        assertThat(pooledTags.size(), is(1));
        assertThat(tag.getTaggedCount(), is(1));
        Set<NewTag> newTags = tags.getNewTags();
        assertThat(newTags.size(), is(1));
    }
    
    @Test
    public void processTags_수정() throws Exception {
        // given
        Tag java = createTaggedTag("java");
        Tag spring = createTaggedTag("spring");
        String plainTags = "java newTags";
        Set<Tag> originalTags = Sets.newHashSet(java, spring);
        when(tagRepository.findByName(java.getName())).thenReturn(java);
                
        // when
        Tags tags = dut.processTags(originalTags, plainTags);
        
        // then
        Set<Tag> pooledTags = tags.getPooledTags();
        assertThat(pooledTags.size(), is(1));
        assertThat(java.getTaggedCount(), is(1));
        assertThat(spring.getTaggedCount(), is(0));
        Set<NewTag> newTags = tags.getNewTags();
        assertThat(newTags.size(), is(1));
    }

    private Tag createTaggedTag(String tagName) {
        Tag tag = new Tag(tagName);
        tag.tagged();
        return tag;
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
