package net.slipp.support.jpa;

import javax.persistence.Persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestPersistenceJPAConfig.class })
@TestPropertySource("classpath:application-properties.xml")
public class SchemaGenerator {
	@Test
	public void generateSchema() throws Exception {
		Persistence.generateSchema("test.slipp.qna", null);
	}
}
