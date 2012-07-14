package net.slipp.domain.qna;

import javax.annotation.Resource;

import net.slipp.domain.user.SocialUser;
import net.slipp.repository.qna.AnswerRepository;
import net.slipp.repository.qna.QuestionRepository;
import net.slipp.repository.qna.TagRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service("qnaService")
@Transactional
public class QnaService {
	@Resource (name = "tagRepository")
	private TagRepository tagRepository;
	
	@Resource (name = "questionRepository")
	private QuestionRepository questionRepository;
	
	@Resource (name = "answerRepository")
	private AnswerRepository answerRepository;
	
	public void createQuestion(SocialUser loginUser, Question question) {
		Assert.notNull(loginUser, "loginUser should be not null!");
		Assert.notNull(question, "question should be not null!");
		
		question.writedBy(loginUser);
		question.initializeTags(tagRepository);
		questionRepository.save(question);
	}
	
	public void updateQuestion(SocialUser loginUser, Question newQuestion) {
		Assert.notNull(loginUser, "loginUser should be not null!");
		Assert.notNull(newQuestion, "question should be not null!");
		
		Question question = questionRepository.findOne(newQuestion.getQuestionId());
		if (!question.isWritedBy(loginUser)) {
			throw new AccessDeniedException(loginUser + " is not owner!");
		}
		
		question.writedBy(loginUser);
		question.update(newQuestion);
		question.initializeTags(tagRepository);
		questionRepository.save(question);
	}
	
	public void deleteQuestion(SocialUser loginUser, Long questionId) {
		Assert.notNull(loginUser, "loginUser should be not null!");
		Assert.notNull(questionId, "questionId should be not null!");
		
		Question question = questionRepository.findOne(questionId);
		if (!question.isWritedBy(loginUser)) {
			throw new AccessDeniedException(loginUser + " is not owner!");
		}
		question.delete();
		questionRepository.save(question);
	}
	
	public Page<Question> findsByTag(String name, Pageable pageable) {
		return questionRepository.findsByTag(name, pageable);
	}
	
	public Page<Question> findsQuestion(Pageable pageable) {
		return questionRepository.findAll(QnaSpecifications.equalsIsDelete(false), pageable);
	}
	
	public Question findByQuestionId(Long id) {
		return questionRepository.findOne(id);
	}
	
	public Iterable<Tag> findsTag() {
		return tagRepository.findAll();
	}

	public void createAnswer(SocialUser user, Long questionId, Answer answer) {
		Question question = questionRepository.findOne(questionId);
		answer.writedBy(user);
		answer.answerTo(question);
		answerRepository.save(answer);
	}

	public void deleteAnswer(SocialUser loginUser, Long questionId, Long answerId) {
		Assert.notNull(loginUser, "loginUser should be not null!");
		Assert.notNull(questionId, "questionId should be not null!");
		Assert.notNull(answerId, "answerId should be not null!");
		
		Answer answer = answerRepository.findOne(answerId);
		if (!answer.isWritedBy(loginUser)) {
			throw new AccessDeniedException(loginUser + " is not owner!");
		}
		answerRepository.delete(answer);
		Question question = questionRepository.findOne(questionId);
		question.decreaseAnswerCount();
	}
}
