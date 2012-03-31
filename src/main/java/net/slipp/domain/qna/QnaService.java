package net.slipp.domain.qna;

import javax.annotation.Resource;

import net.slipp.repository.qna.QuestionRepository;
import net.slipp.repository.qna.TagRepository;
import net.slipp.social.connect.SocialUser;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("qnaService")
@Transactional
public class QnaService {
	@Resource (name = "tagRepository")
	private TagRepository tagRepository;
	
	@Resource (name = "questionRepository")
	private QuestionRepository questionRepository;
	
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
	
	public Iterable<Question> findsQuestion() {
		return questionRepository.findAll();
	}
	
	public Question findByQuestionId(Long id) {
		return questionRepository.findOne(id);
	}
	
	public Iterable<Tag> findsTag() {
		return tagRepository.findAll();
	}

}
