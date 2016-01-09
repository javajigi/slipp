package net.slipp.support.utils

import java.util.Date

import com.google.common.collect.Lists
import net.slipp.domain.fb.FacebookComment
import net.slipp.support.test.UnitTest
import org.junit.Test
import org.slf4j.LoggerFactory
import org.springframework.ui.freemarker.{FreeMarkerConfigurationFactoryBean, FreeMarkerTemplateUtils}
import scala.collection.JavaConversions._

class FreemarkerTest extends UnitTest {
  private val logger = LoggerFactory.getLogger(classOf[FreemarkerTest])

  @Test def createFBComments() {
    val comment = new FacebookComment("1", "123", "javajigi", new Date, "이건 댓글이다.\n정말?", null, null)
    val comments = Lists.newArrayList[FacebookComment](comment)
    val template = createTemplate("fbcomments.ftl", Map("comments" -> comments))
    logger.debug(s"${template}")
  }

  @Test def createPassword() {
    val template = createTemplate("passwordinfo.ftl", Map("user" -> aSomeUser()))
    logger.debug(s"${template}")
  }

  private def createTemplate(fileName: String, params: java.util.Map[String, Object]) = {
    val template = configuration.getTemplate(fileName)
    FreeMarkerTemplateUtils.processTemplateIntoString(template, params)
  }

  private def configuration = {
    val freemarkerConfiguration: FreeMarkerConfigurationFactoryBean = new FreeMarkerConfigurationFactoryBean
    freemarkerConfiguration.setTemplateLoaderPath("classpath:templates")
    freemarkerConfiguration.setDefaultEncoding("UTF-8")
    freemarkerConfiguration.afterPropertiesSet()
    freemarkerConfiguration.getObject
  }
}
