package net.slipp.support.jpa

import java.io.File
import java.nio.file.{Paths, Files}

import net.slipp.domain.notification.Notification
import net.slipp.domain.qna.{ScoreLike, Attachment, Answer, Question}
import net.slipp.domain.smalltalk.{SmallTalkComment, SmallTalk}
import net.slipp.domain.tag.{Tag, TaggedHistory}
import net.slipp.domain.user.SocialUser
import org.apache.commons.io.FileUtils
import org.hibernate.cfg.Configuration
import org.hibernate.tool.hbm2ddl.SchemaExport
import org.junit.Assert.assertTrue
import org.junit.Test

class SchemaGeneratorTest {
  private val SCHEMA_DIR = "src/test/resources/schema"
  private val SCHEMA_FILE = s"${SCHEMA_DIR}/create_schema.sql"

  @Test def generateSchemaAndCompare {
    if (!Files.exists(Paths.get(SCHEMA_DIR))) {
      Files.createDirectory(Paths.get(SCHEMA_DIR))
    }

    val entities: Array[Class[_]] = Array(
      classOf[SocialUser],
      classOf[Question],
      classOf[Answer],
      classOf[Tag],
      classOf[Attachment],
      classOf[ScoreLike],
      classOf[TaggedHistory],
      classOf[Notification],
      classOf[SmallTalk],
      classOf[SmallTalkComment]
    )

    generate(entities, SCHEMA_FILE)
  }

  private def generate(entities: Array[Class[_]], outputfile: String) {
    val cfg: Configuration = new Configuration
    cfg.setProperty("hibernate.dialect", "net.slipp.support.jpa.Mysql5BitBooleanDialect")
    entities.foreach(c => cfg.addAnnotatedClass(c))
    val export: SchemaExport = new SchemaExport(cfg)
    export.setDelimiter(";")
    export.setOutputFile(outputfile)
    export.setFormat(true)
    export.execute(true, false, false, false)
  }
}
