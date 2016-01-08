package net.slipp.support.jpa

import java.io.File
import javax.persistence.Embeddable
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.MappedSuperclass
import javax.persistence.Transient
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.mysema.query.codegen.GenericExporter
import com.mysema.query.codegen.Keywords

class QueryDslTest {
  private var log: Logger = LoggerFactory.getLogger(classOf[QueryDslTest])

  @Test def export {
    val exporter: GenericExporter = new GenericExporter
    exporter.setKeywords(Keywords.JPA)
    exporter.setEntityAnnotation(classOf[Entity])
    exporter.setEmbeddableAnnotation(classOf[Embeddable])
    exporter.setEmbeddedAnnotation(classOf[Embedded])
    exporter.setSupertypeAnnotation(classOf[MappedSuperclass])
    exporter.setSkipAnnotation(classOf[Transient])
    exporter.setCreateScalaSources(true)
    exporter.setTargetFolder(new File("target/generated-sources/scala"))
    exporter.export("net.slipp.ndomain")
  }
}
