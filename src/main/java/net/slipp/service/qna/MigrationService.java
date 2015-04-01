package net.slipp.service.qna;

import java.util.List;

import javax.annotation.Resource;

import net.slipp.domain.qna.Question;
import net.slipp.repository.qna.QuestionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MigrationService {
	@Resource(name = "questionRepository")
	private QuestionRepository questionRepository;
	
	public void convertConfluenceToMarkdown() {
		List<Question> questions = questionRepository.findAll();
		for (Question question : questions) {
			question.convertWiki();
		}
	}
}