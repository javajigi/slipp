package net.slipp.service.qna;

import javax.annotation.Resource;

import net.slipp.repository.qna.QuestionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MigrationService {
	@Resource(name = "questionRepository")
	private QuestionRepository questionRepository;
	
}