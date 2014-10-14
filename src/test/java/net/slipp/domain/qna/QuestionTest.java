package net.slipp.domain.qna;

import static net.slipp.domain.qna.AnswerBuilder.anAnswer;
import static net.slipp.domain.qna.QuestionBuilder.aQuestion;
import static net.slipp.domain.tag.TagBuilder.aTag;
import static net.slipp.domain.tag.TagTest.JAVA;
import static net.slipp.domain.tag.TagTest.JAVASCRIPT;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Set;

import net.slipp.domain.ProviderType;
import net.slipp.domain.tag.Tag;
import net.slipp.domain.user.SocialUser;
import net.slipp.domain.user.SocialUserBuilder;
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
	public void 질문한_사람이_같다() throws Exception {
		SocialUser writer = new SocialUser(10);
		dut = aQuestion().withWriter(writer).build();

		assertThat(dut.isWritedBy(writer), is(true));
	}

	@Test
	public void 질문한_사람이_다르다() throws Exception {
		SocialUser writer = new SocialUser(10);
		dut = aQuestion().withWriter(writer).build();

		boolean actual = dut.isWritedBy(new SocialUser(11));
		assertThat(actual, is(false));
	}

	@Test
	public void 새로운_질문() throws Exception {
		// given
		SocialUser writer = new SocialUser();
		String title = "title";
		String contents = "contents";
		Tag java = JAVA;

		// when
		Question newQuestion = new Question(writer, title, contents, Sets.newHashSet(java));

		// then
		assertThat(newQuestion.getTitle(), is(title));
		assertThat(newQuestion.getContents(), is(contents));
		assertThat(newQuestion.hasTag(java), is(true));
		assertThat(newQuestion.getDenormalizedTags(), is(java.getName()));
	}

	@Test
	public void 질문_수정() throws Exception {
		// given
		SocialUser writer = new SocialUser();
		Tag java = JAVA;
		Question newQuestion = new Question(writer, "title", "contents", Sets.newHashSet(java));

		// when
		String updateTitle = "update title";
		String updateContents = "update contents";
		Tag javascript = JAVASCRIPT;
		newQuestion.update(writer, updateTitle, updateContents, Sets.newHashSet(java, javascript));
		assertThat(newQuestion.getTitle(), is(updateTitle));
		assertThat(newQuestion.getContents(), is(updateContents));
		assertThat(newQuestion.hasTag(java), is(true));
	}

	@Test
	public void 화제의_답변이_존재한다() throws Exception {
		Question dut = aQuestion().withAnswer(anAnswer().withTotalLiked(1).build())
				.withAnswer(anAnswer().withTotalLiked(0).build()).withAnswer(anAnswer().withTotalLiked(3).build())
				.build();
		Answer bestAnswer = dut.getBestAnswer();
		assertThat(bestAnswer.getSumLike(), is(3));
	}

	@Test
	public void 화제의_답변이_존재하지_않는다() throws Exception {
		Question dut = aQuestion().withAnswer(anAnswer().withTotalLiked(1).build())
				.withAnswer(anAnswer().withTotalLiked(0).build()).build();

		assertThat(dut.getBestAnswer(), is(nullValue()));
	}

	@Test
	public void 답변이_존재하지_않는다() throws Exception {
		Question dut = aQuestion().build();
		assertThat(dut.getBestAnswer(), is(nullValue()));
	}
	
	@Test
	public void 질문에_답변을_한다() throws Exception {
		SocialUser writer1 = new SocialUser(10);
		Question dut = aQuestion().withWriter(writer1).build();
		assertThat(dut.getLatestParticipant(), is(writer1));
		
		SocialUser writer2 = new SocialUser(11);
		Answer answer = anAnswer().with(writer2).build();
		dut.newAnswered(answer);
		assertThat(dut.getLatestParticipant(), is(writer2));
	}
	
	@Test
	public void delete_answer() throws Exception {
		SocialUser writer2 = new SocialUser(11);
		Answer answer1 = anAnswer().with(writer2).build();
		SocialUser writer3 = new SocialUser(12);
		Answer answer2 = anAnswer().with(writer3).build();
		
		SocialUser writer1 = new SocialUser(10);
		Question dut = aQuestion()
				.withWriter(writer1)
				.withAnswer(answer1)
				.withAnswer(answer2)
				.build();
		assertThat(dut.getAnswerCount(), is(2));
		
		dut.deAnswered(answer2);
		// assertThat(dut.getLatestParticipant(), is(writer2));
	}
	
	@Test
	public void 질문을_삭제한다() throws Exception {
		Tag java = aTag().withName("java").build();
		SocialUser writer = new SocialUser();
		Question dut = aQuestion().withWriter(writer).withTag(java).build();
		dut.delete(writer);
		assertThat(dut.isDeleted(), is(true));
		assertThat(java.getTaggedCount(), is(0));
	}

	@Test
	public void differentTags() throws Exception {
		// given
		Tag java = aTag().withName("java").withTaggedCount(3).build();
		Tag javascript = aTag().withName("javascript").withTaggedCount(2).build();
		Tag newTag = aTag().withName("newTag").build();
		
		Question question = aQuestion().withTag(java).withTag(javascript).build();
		
		// when
		Set<Tag> differentTags = question.differentTags(Sets.newHashSet(javascript, newTag));
		
		// then
		assertThat(differentTags.size(), is(1));
		assertTrue(differentTags.contains(newTag));
	}

	@Test
	public void detaggedTags() throws Exception {
		Tag java = aTag().withName("java").withTaggedCount(3).build();
		Tag javascript = aTag().withName("javascript").withTaggedCount(2).build();
		Set<Tag> originalTags = Sets.newHashSet(java, javascript);
		Question.detaggedTags(originalTags);

		assertThat(java.getTaggedCount(), is(2));
		assertThat(javascript.getTaggedCount(), is(1));
	}

	@Test
	public void taggedTags() throws Exception {
		Tag java = aTag().withName("java").withTaggedCount(3).build();
		Tag javascript = aTag().withName("javascript").withTaggedCount(2).build();
		Set<Tag> originalTags = Sets.newHashSet(java, javascript);
		Question.taggedTags(originalTags);

		assertThat(java.getTaggedCount(), is(4));
		assertThat(javascript.getTaggedCount(), is(3));
	}
	
    @Test
    public void connected() throws Exception {
        SocialUser writer = new SocialUser(10);
        writer.setProviderId("facebook");
        dut = aQuestion()
                .withWriter(writer)
              .build();
        
        String postId = "123456";
        SnsConnection actual = dut.connected(postId);

        SnsConnection expected = new SnsConnection(ProviderType.facebook, postId);
        assertThat(actual, is(expected));
    }
    
    @Test
    public void getTotalAnswerCount() throws Exception {
        SocialUser writer1 = new SocialUserBuilder().withProviderType(ProviderType.facebook).build();
        Question dut = aQuestion()
                .withWriter(writer1)
                .withAnswer(new Answer())
                .withAnswer(new Answer())
                .build();
        SnsConnection connection = dut.connected("12345");
        connection.updateAnswerCount(3);
        assertThat(dut.getTotalAnswerCount(), is(5));
    }
}
