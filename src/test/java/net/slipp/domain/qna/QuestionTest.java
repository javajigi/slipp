package net.slipp.domain.qna;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Set;

import net.slipp.domain.user.SocialUser;

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
	private TagProcessor tagProcessor;
	
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
		Set<Tag> pooledTag = Sets.newHashSet(new Tag("java"), new Tag("javascript"));
		when(tagProcessor.processTags("java javascript")).thenReturn(pooledTag);
		
		Question dut = new QuestionBuilder().tags("java javascript").build();
		dut.initializeTags(tagProcessor);
		assertThat(dut.getTags().size(), is(2));
	}
	
	@Test
	public void updateQuestion() throws Exception {
		Set<Tag> firstTags = Sets.newHashSet(new Tag("java"), new Tag("javascript"));
		when(tagProcessor.processTags("java javascript")).thenReturn(firstTags);
		Question dut = new QuestionBuilder().tags("java javascript").build();
		dut.initializeTags(tagProcessor);
		
		dut.setPlainTags("maven java eclipse");
		Set<Tag> updatedTags = Sets.newHashSet(new Tag("maven"), new Tag("java"), new Tag("eclipse"));
		when(tagProcessor.processTags("maven java eclipse")).thenReturn(updatedTags);
		dut.initializeTags(tagProcessor);
		assertThat(dut.getTags().size(), is(3));
	}
	
	@Test
	public void contents() throws Exception {
		String contents = "this is contents";
		dut.setContents(contents);
		assertThat(dut.getContents(), is(contents));
	}
}
