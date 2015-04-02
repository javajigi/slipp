package net.slipp.service.qna;

import java.util.List;

import javax.annotation.Resource;

import net.slipp.domain.qna.Answer;
import net.slipp.domain.qna.Question;
import net.slipp.repository.qna.AnswerRepository;
import net.slipp.repository.qna.QuestionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MigrationService {
	@Resource(name = "questionRepository")
	private QuestionRepository questionRepository;
	
	@Resource(name = "answerRepository")
	private AnswerRepository answerRepository;
	
	public void convertConfluenceToMarkdown() {
		List<Question> questions = questionRepository.findAll();
		for (Question question : questions) {
			question.convertWiki();
		}
		
		List<Answer> answers = answerRepository.findAll();
		for (Answer answer : answers) {
			answer.convertWiki();
		}
	}
}