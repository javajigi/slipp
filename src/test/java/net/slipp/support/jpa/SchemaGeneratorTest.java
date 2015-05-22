package net.slipp.support.jpa;

import org.junit.Test;

public class SchemaGeneratorTest {
	@Test
	public void schemaGenerator() throws Exception {
		SchemaGenerator generator = new SchemaGenerator("net.slipp.domain.qna");
		generator.generate();
	}
}
