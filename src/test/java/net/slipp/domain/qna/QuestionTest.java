package net.slipp.domain.qna;

import static net.slipp.domain.qna.AnswerBuilder.*;
import static net.slipp.domain.qna.QuestionBuilder.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

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
		Tag java = new Tag("java");

		// when
		Question newQuestion = new Question(writer, title, contents,
				Sets.newHashSet(java));

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
		Tag java = new Tag("java");
		Question newQuestion = new Question(writer, "title", "contents",
				Sets.newHashSet(java));

		// when
		String updateTitle = "update title";
		String updateContents = "update contents";
		Tag javascript = new Tag("javascript");
		newQuestion.update(writer, updateTitle, updateContents,
				Sets.newHashSet(java, javascript));
		assertThat(newQuestion.getTitle(), is(updateTitle));
		assertThat(newQuestion.getContents(), is(updateContents));
		assertThat(newQuestion.hasTag(java), is(true));
		assertThat(newQuestion.getDenormalizedTags(), is(java.getName() + ","
				+ javascript.getName()));
	}

	@Test
	public void 태그추가() throws Exception {
		Tag java = new Tag("java");
		Tag javascript = new Tag("javascript");
		Question question = aQuestion().withTag(java).withTag(javascript)
				.build();
		Set<Tag> tags = question.getTags();

		assertThat(tags.contains(java), is(true));
		assertThat(tags.contains(javascript), is(true));
		assertThat(java.getTaggedCount(), is(1));
		assertThat(javascript.getTaggedCount(), is(1));
	}

	@Test
	public void 화제의_답변이_존재한다() throws Exception {
		Question dut = aQuestion()
				.withAnswer(anAnswer().withTotalLiked(1).build())
				.withAnswer(anAnswer().withTotalLiked(0).build())
				.withAnswer(anAnswer().withTotalLiked(3).build()).build();
		Answer bestAnswer = dut.getBestAnswer();
		assertThat(bestAnswer.getSumLike(), is(3));
	}

	@Test
	public void 화제의_답변이_존재하지_않는다() throws Exception {
		Question dut = aQuestion()
				.withAnswer(anAnswer().withTotalLiked(1).build())
				.withAnswer(anAnswer().withTotalLiked(0).build()).build();
		
		assertThat(dut.getBestAnswer(), is(nullValue()));
	}

	@Test
	public void 답변이_존재하지_않는다() throws Exception {
		Question dut = aQuestion().build();
		assertThat(dut.getBestAnswer(), is(nullValue()));
	}
}
