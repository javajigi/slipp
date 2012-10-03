package net.slipp.domain.qna;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;

import java.util.Set;

import net.slipp.domain.tag.Tag;
import net.slipp.domain.user.SocialUser;
import net.slipp.repository.tag.TagRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Sets;

@RunWith(MockitoJUnitRunner.class)
public class QuestionTest {
	private Question dut;
	
	@Mock
	private TagRepository tagRepository;
	
	@Before
	public void setup() {
		dut = new Question();
	}
	
	@Test
	public void isWritedBy_sameUser() throws Exception {
		// given
		SocialUser user = new SocialUser(10);
		dut.writedBy(user);
		
		// when
		boolean actual = dut.isWritedBy(user);
		
		// then
		assertThat(actual, is(true));
	}
	
	@Test
	public void isWritedBy_differentUser() throws Exception {
		// given
		SocialUser user = new SocialUser(10);
		dut.writedBy(new SocialUser(11));
		
		// when
		boolean actual = dut.isWritedBy(user);
		
		// then
		assertThat(actual, is(false));
	}
	
	@Test
	public void newQuestion() throws Exception {
		SocialUser loginUser = new SocialUser();
		Question questionDto = new QuestionBuilder().tags("java javascript").build();
		Question newQuestion = Question.newQuestion(loginUser, questionDto);
		
	}
	
//	@Test
//	public void newQuestion() throws Exception {
//		when(tagRepository.processTags("java javascript")).thenReturn(pooledTag);
//		
//		Question dut = new QuestionBuilder().tags("java javascript").build();
//		dut.processTags(tagRepository);
//		assertThat(dut.getTags().size(), is(2));
//	}
	
//	@Test
//	public void updateQuestion() throws Exception {
//		Set<Tag> firstTags = Sets.newHashSet(new Tag("java"), new Tag("javascript"));
//		when(tagProcessor.processTags("java javascript")).thenReturn(firstTags);
//		Question dut = new QuestionBuilder().tags("java javascript").build();
//		dut.processTags(tagProcessor);
//		
//		dut.setPlainTags("maven java eclipse");
//		Set<Tag> updatedTags = Sets.newHashSet(new Tag("maven"), new Tag("java"), new Tag("eclipse"));
//		when(tagProcessor.processTags("maven java eclipse")).thenReturn(updatedTags);
//		dut.processTags(tagProcessor);
//		assertThat(dut.getTags().size(), is(3));
//	}
	
	@Test
	public void tag() throws Exception {
		Tag tag = new Tag("newTag");
		Question question = new Question();
		question.tag(tag);
		
		Set<Tag> tags = Sets.newHashSet(tag);
		assertThat(question.getTags(), is(tags));
		assertThat(tag.getTaggedCount(), is(1));
	}
	
	@Test
	public void contents() throws Exception {
		String contents = "this is contents";
		dut.setContents(contents);
		assertThat(dut.getContents(), is(contents));
	}
}
