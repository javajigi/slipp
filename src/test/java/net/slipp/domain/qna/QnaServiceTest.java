package net.slipp.domain.qna;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.slipp.domain.user.SocialUser;
import net.slipp.repository.qna.QuestionRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.access.AccessDeniedException;

@RunWith(MockitoJUnitRunner.class)
public class QnaServiceTest {
	@Mock
	private QuestionRepository questionRepository;
	
	@InjectMocks
	private QnaService dut = new QnaService();
	
	@Test
	public void updateQuestion_sameUser() {
		// given
		SocialUser loginUser = new SocialUser(10);
		Question question = new Question(1L);
		question.writedBy(loginUser);
		question.setPlainTags("");
		when(questionRepository.findOne(question.getQuestionId())).thenReturn(question);
		
		// when
		dut.updateQuestion(loginUser, question);
		
		// then
		verify(questionRepository).save(question);
	}
	
	@Test (expected=AccessDeniedException.class)
	public void updateQuestion_differentUser() {
		// given
		SocialUser loginUser = new SocialUser(10);
		Question question = new Question(1L);
		question.writedBy(new SocialUser(11));
		question.setPlainTags("");
		when(questionRepository.findOne(question.getQuestionId())).thenReturn(question);
		
		// when
		dut.updateQuestion(loginUser, question);
	}
}
