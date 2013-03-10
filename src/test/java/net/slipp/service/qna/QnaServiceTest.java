package net.slipp.service.qna;

import static net.slipp.domain.tag.TagTest.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Set;

import net.slipp.domain.qna.Answer;
import net.slipp.domain.qna.Question;
import net.slipp.domain.qna.QuestionDto;
import net.slipp.domain.tag.Tag;
import net.slipp.domain.user.SocialUser;
import net.slipp.repository.qna.AnswerRepository;
import net.slipp.repository.qna.QuestionRepository;
import net.slipp.service.rank.ScoreLikeService;
import net.slipp.service.tag.TagService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.access.AccessDeniedException;

import com.google.common.collect.Sets;

@RunWith(MockitoJUnitRunner.class)
public class QnaServiceTest {
	@Mock
	private QuestionRepository questionRepository;
	
	@Mock
	private AnswerRepository answerRepository;
	
	@Mock
	private TagService tagService;
	@Mock
	private ScoreLikeService scoreLikeService;
	
	@InjectMocks
	private QnaService dut = new QnaService();
	
	@Test
	public void updateQuestion_sameUser() {
		// given
		SocialUser loginUser = new SocialUser(10);
		QuestionDto dto = new QuestionDto(1L, "title", "contents", "java javascript");
		Set<Tag> originalTags = Sets.newHashSet(JAVA);
		Question existedQuestion = new Question(1L, loginUser, dto.getTitle(), dto.getContents(), originalTags);
		when(questionRepository.findOne(dto.getQuestionId())).thenReturn(existedQuestion);
		Set<Tag> tags = Sets.newHashSet(JAVA, NEWTAG);
		when(tagService.processTags(dto.getPlainTags())).thenReturn(tags);
		
		// when
		dut.updateQuestion(loginUser, dto);
	}
	
	@Test
	public void deleteAnswer_sameUser() throws Exception {
		// given
		SocialUser loginUser = new SocialUser(10);
		Answer answer = new Answer(2L);
		answer.writedBy(loginUser);
		Question question = new Question(1L, loginUser, null, null, new HashSet<Tag>());
		when(answerRepository.findOne(answer.getAnswerId())).thenReturn(answer);
		when(questionRepository.findOne(question.getQuestionId())).thenReturn(question);
		
		// when
		dut.deleteAnswer(loginUser, question.getQuestionId(), answer.getAnswerId());
		
		// then
		verify(answerRepository).delete(answer);
	}
	
	@Test (expected=AccessDeniedException.class)
	public void deleteAnswer_differentUser() throws Exception {
		// given
		SocialUser loginUser = new SocialUser(10);
		Long questionId = 1L;
		Answer answer = new Answer(2L);
		answer.writedBy(new SocialUser(11));
		when(answerRepository.findOne(answer.getAnswerId())).thenReturn(answer);
		
		// when
		dut.deleteAnswer(loginUser, questionId, answer.getAnswerId());
	}
	
	@Test
	public void likeAnswer_() throws Exception {
		SocialUser loginUser = new SocialUser(10);
		Answer answer = new Answer(2L);
		answer.writedBy(loginUser);
		when(answerRepository.findOne(answer.getAnswerId())).thenReturn(answer);
		dut.likeAnswer(loginUser, answer.getAnswerId());
	}
}

