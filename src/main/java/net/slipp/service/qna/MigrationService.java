package net.slipp.service.qna;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import net.slipp.domain.qna.Question;
import net.slipp.domain.qna.SnsConnection;
import net.slipp.repository.qna.QuestionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MigrationService {
	@Resource(name = "questionRepository")
	private QuestionRepository questionRepository;
	
	public void removeIdSnsConnection() {
		List<Question> questions = questionRepository.findAll();
		for (Question question : questions) {
			Collection<SnsConnection> connections = question.getSnsConnection();
			for (SnsConnection snsConnection : connections) {
				snsConnection.removeId();
			}
		}
	}
}