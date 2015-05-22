package net.slipp.support.jpa;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;

import javax.persistence.Persistence;
import javax.persistence.spi.PersistenceUnitTransactionType;

import org.hibernate.boot.registry.classloading.internal.ClassLoaderServiceImpl;
import org.hibernate.cfg.Configuration;
import org.hibernate.jpa.boot.internal.ParsedPersistenceXmlDescriptor;
import org.hibernate.jpa.boot.internal.PersistenceXmlParser;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestPersistenceJPAConfig.class })
public class SchemaGeneratorTest {
	
	@Test
	public void generateSchema() throws Exception {
		Persistence.generateSchema("slipp.qna", null);
	}
	
	@Test
	public void generate() throws Exception {
		PersistenceXmlParser parser = new PersistenceXmlParser(new ClassLoaderServiceImpl(), PersistenceUnitTransactionType.RESOURCE_LOCAL);
	    List<ParsedPersistenceXmlDescriptor> allDescriptors = parser.doResolve(new HashMap<>());

	    for (ParsedPersistenceXmlDescriptor descriptor : allDescriptors) {
	        Configuration cfg = new Configuration();
	        cfg.setProperty("hibernate.hbm2ddl.auto", "create");
	        cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
	        cfg.setProperty("hibernate.id.new_generator_mappings", "true");

	        List<String> managedClassNames = descriptor.getManagedClassNames();
	        for (String className : managedClassNames) {
	            try {
	                cfg.addAnnotatedClass(Class.forName(className));
	            } catch (ClassNotFoundException e) {
	                System.out.println("Class not found: " + className);
	            }
	        }

	        SchemaExport export = new SchemaExport(cfg);
	        export.setDelimiter(";");
	        export.setOutputFile("create_schema.sql");
	        export.setFormat(true);
	        export.execute(true, false, false, false);
	    }
	}
	
	@Test
	public void schemaGenerator() throws Exception {
		SchemaGenerator generator = new SchemaGenerator("net.slipp.domain.qna");
		generator.generate();
	}
}
