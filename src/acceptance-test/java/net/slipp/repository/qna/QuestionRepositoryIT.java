package net.slipp.repository.qna;

import net.slipp.domain.qna.Question_;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class QuestionRepositoryIT {
	@Autowired
	private QuestionRepository dut;

	@Test
	public void findsByTag() {
		// Page Index는 0부터 시작한다.
		PageRequest page = new PageRequest(0, 5, Direction.DESC, Question_.createdDate.getName());
		String name = "eclipse";
		dut.findsByTag(name, page);
	}
}
