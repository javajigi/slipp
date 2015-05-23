package net.slipp.support.jpa;

import java.io.File;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.junit.Test;

import com.mysema.query.codegen.GenericExporter;
import com.mysema.query.codegen.Keywords;

public class QueryDslTest {
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
}
