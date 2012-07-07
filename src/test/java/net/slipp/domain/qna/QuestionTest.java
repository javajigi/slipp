package net.slipp.domain.qna;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import net.slipp.domain.user.SocialUser;
import net.slipp.repository.qna.MockTagRepository;

import org.junit.Before;
import org.junit.Test;

public class QuestionTest {
	private Question dut;
	private MockTagRepository tagRepository;
	
	@Before
	public void setup() {
		dut = new Question();
		tagRepository = new MockTagRepository();
	}
	
	@Test
	public void isWritedBy_SameUser() throws Exception {
		// given
		SocialUser user = new SocialUser();
		user.setId(10);
		dut.writedBy(user);
		
		// when
		boolean actual = dut.isWritedBy(user);
		
		// then
		assertThat(actual, is(true));
	}
	
	@Test
	public void newQuestion() throws Exception {
		Question dut = new QuestionBuilder().tags("java javascript").build();
		dut.initializeTags(tagRepository);
		assertThat(dut.getTags().size(), is(2));
		assertThat(tagRepository.findByName("java").getTaggedCount(), is(1));
		assertThat(tagRepository.findByName("javascript").getTaggedCount(), is(1));
	}
	
	@Test
	public void updateQuestion() throws Exception {
		Question dut = new QuestionBuilder().tags("java javascript").build();
		dut.initializeTags(tagRepository);
		dut.setPlainTags("maven java eclipse");
		dut.initializeTags(tagRepository);
		assertThat(dut.getTags().size(), is(3));
		
		assertThat(tagRepository.findByName("maven").getTaggedCount(), is(1));
		assertThat(tagRepository.findByName("java").getTaggedCount(), is(1));
		assertThat(tagRepository.findByName("eclipse").getTaggedCount(), is(1));
		assertThat(tagRepository.findByName("javascript").getTaggedCount(), is(0));
	}
	
	@Test
	public void contents() throws Exception {
		String contents = "this is contents";
		dut.setContents(contents);
		assertThat(dut.getContents(), is(contents));
	}
}
