package net.slipp.support.jpa

import org.junit.Assert.assertTrue
import java.io.File
import org.apache.commons.io.FileUtils
import org.hibernate.cfg.Configuration
import org.hibernate.tool.hbm2ddl.SchemaExport
import org.junit.Test

object SchemaGeneratorTest {
  private val SCHEMA_FROM_JAVA: String = "src/test/resources/schema/create_schema_java.sql"
  private val SCHEMA_FROM_SCALA: String = "src/test/resources/schema/create_schema_scala.sql"
}

class SchemaGeneratorTest {
  @Test
  @throws(classOf[Exception])
  def generateSchemaAndCompare {
    assertTrue(FileUtils.contentEquals(new File(SchemaGeneratorTest.SCHEMA_FROM_JAVA), new File(SchemaGeneratorTest.SCHEMA_FROM_SCALA)))
  }

  private def generate(entity: Class[_], outputfile: String) {
    val cfg: Configuration = new Configuration
    cfg.setProperty("hibernate.hbm2ddl.auto", "create")
    cfg.setProperty("hibernate.dialect", "net.slipp.support.jpa.Mysql5BitBooleanDialect")
    cfg.addAnnotatedClass(entity)
    val export: SchemaExport = new SchemaExport(cfg)
    export.setDelimiter(";")
    export.setOutputFile(outputfile)
    export.setFormat(true)
    export.execute(true, false, false, false)
  }
}
