package net.slipp.support.jpa;

import static org.junit.Assert.assertTrue;

import java.io.File;

import net.slipp.domain.tag.TaggedHistory;
import net.slipp.ndomain.tag.NTaggedHistory;

import org.apache.commons.io.FileUtils;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.Test;

public class SchemaGeneratorTest {
	private static final String SCHEMA_FROM_JAVA = "src/test/resources/schema/create_schema_java.sql";
	private static final String SCHEMA_FROM_SCALA = "src/test/resources/schema/create_schema_scala.sql";

	@Test
	public void generateSchemaAndCompare() throws Exception {
		generate(TaggedHistory.class, SCHEMA_FROM_JAVA);
		generate(NTaggedHistory.class, SCHEMA_FROM_SCALA);

		assertTrue(FileUtils.contentEquals(new File(SCHEMA_FROM_JAVA), new File(SCHEMA_FROM_SCALA)));
	}

	private void generate(Class<?> entity, String outputfile) {
		Configuration cfg = new Configuration();
		cfg.setProperty("hibernate.hbm2ddl.auto", "create");
		cfg.setProperty("hibernate.dialect", "net.slipp.support.jpa.Mysql5BitBooleanDialect");
		
		cfg.addAnnotatedClass(entity);
		
		SchemaExport export = new SchemaExport(cfg);
        export.setDelimiter(";");
        export.setOutputFile(outputfile);
        export.setFormat(true);
        export.execute(true, false, false, false);
	}
}
