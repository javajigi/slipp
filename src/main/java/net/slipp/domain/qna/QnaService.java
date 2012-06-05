package net.slipp.domain.qna;

import javax.annotation.Resource;

import net.slipp.domain.user.SocialUser;
import net.slipp.repository.qna.AnswerRepository;
import net.slipp.repository.qna.QuestionRepository;
import net.slipp.repository.qna.TagRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("qnaService")
@Transactional
public class QnaService {
	@Resource (name = "tagRepository")
	private TagRepository tagRepository;
	
	@Resource (name = "questionRepository")
	private QuestionRepository questionRepository;
	
	@Resource (name = "answerRepository")
	private AnswerRepository answerRepository;
	
	public void createQuestion(SocialUser user, Question question) {
		question.writedBy(user);
		question.initializeTags(tagRepository);
		questionRepository.save(question);
	}
	
	public void updateQuestion(SocialUser user, Question newQuestion) {
		Question question = questionRepository.findOne(newQuestion.getQuestionId());
		question.writedBy(user);
		question.update(newQuestion);
		question.initializeTags(tagRepository);
		questionRepository.save(question);
	}
	
	public Page<Question> findsByTag(String name, Pageable pageable) {
		return questionRepository.findsByTag(name, pageable);
	}
	
	public Iterable<Question> findsQuestion() {
		return questionRepository.findAll();
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
}
