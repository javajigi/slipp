package net.slipp.repository.qna;

import javax.transaction.Transactional;

import net.slipp.domain.qna.Question;
import net.slipp.domain.qna.Question_;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import slipp.config.ApplicationConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
@Transactional
public class QuestionRepositoryIT {
	private static final Logger logger = LoggerFactory.getLogger(QuestionRepositoryIT.class);
	
	@Autowired
	private QuestionRepository dut;

	@Test
	public void findsByTag() {
		// Page Index는 0부터 시작한다.
		PageRequest page = new PageRequest(0, 5, Direction.DESC, Question_.createdDate.getName());
		String name = "eclipse";
		dut.findsByTag(name, page);
	}
	
	@Test
	public void findById() throws Exception {
		Question question = dut.findOne(270L);
		logger.debug("contents : {}", question.getContents());
		String html = new PegDownProcessor(Extensions.FENCED_CODE_BLOCKS).markdownToHtml(question.getContents());
		logger.debug("html : {}", html);
	}
}
