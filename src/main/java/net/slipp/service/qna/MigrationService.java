package net.slipp.service.qna;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import net.slipp.domain.qna.Question;
import net.slipp.domain.qna.SnsConnection;
import net.slipp.domain.tag.Tag;
import net.slipp.repository.qna.QuestionRepository;
import net.slipp.service.tag.TagHelper;
import net.slipp.web.tag.AdminTagController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MigrationService {
	private static final Logger log = LoggerFactory.getLogger(AdminTagController.class);
	
	@Resource(name = "questionRepository")
	private QuestionRepository questionRepository;
	
	public void migration() {
		List<Question> questions = questionRepository.findAll();
		for (Question question : questions) {
			Set<Tag> tags = question.getTags();
			String denormalizedTags = TagHelper.denormalizedTags(tags);
			questionRepository.updateDenormalizedTags(question.getQuestionId(), denormalizedTags);
			log.debug("migration : questionId : {}, denormalizedTags {}", question.getQuestionId(), denormalizedTags);
		}
	}
	
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