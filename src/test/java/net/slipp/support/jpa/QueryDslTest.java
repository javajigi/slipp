package net.slipp.support.jpa;

import java.io.File;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import net.slipp.domain.qna.QQuestion;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.codegen.GenericExporter;
import com.mysema.query.codegen.Keywords;

public class QueryDslTest {
	private static Logger log = LoggerFactory.getLogger(QueryDslTest.class); 
	
	@Test
    public void export() throws Exception {
		GenericExporter exporter = new GenericExporter();
		exporter.setKeywords(Keywords.JPA);
		exporter.setEntityAnnotation(Entity.class);
		exporter.setEmbeddableAnnotation(Embeddable.class);
		exporter.setEmbeddedAnnotation(Embedded.class);        
		exporter.setSupertypeAnnotation(MappedSuperclass.class);
		exporter.setSkipAnnotation(Transient.class);
		exporter.setCreateScalaSources(true);
		exporter.setTargetFolder(new File("target/generated-sources/java"));
		exporter.export("net.slipp.domain");
    }
	
	@Test
	public void getFieldName() throws Exception {
		QQuestion question = QQuestion.question;
		log.debug("fieldName : {}", question.deleted);
	}
}
