package net.slipp.service

import com.google.common.collect.Maps
import freemarker.template.Configuration
import freemarker.template.Template
import net.slipp.domain.tag.Tag
import net.slipp.domain.user.SocialUser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessagePreparator
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils
import javax.annotation.Resource
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import java.io.IOException
import java.util.Map

@Service object MailService {
  private val DEFAULT_ADMIN_EMAIL: String = "javajigi@gmail.com"
}

@Service class MailService {
  private val log: Logger = LoggerFactory.getLogger(classOf[MailService])
  @Resource(name = "mailSender") private[service] var mailSender: JavaMailSender = null
  @Resource(name = "freemarkerConfiguration") private[service] var configuration: Configuration = null

  @Async def sendPasswordInformation(user: SocialUser) {
    val preparator: MimeMessagePreparator = new MimeMessagePreparator() {
      @throws(classOf[MessagingException])
      def prepare(mimeMessage: MimeMessage) {
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail))
        mimeMessage.setFrom(new InternetAddress(MailService.DEFAULT_ADMIN_EMAIL))
        mimeMessage.setSubject("SLiPP에 오신것을 환영합니다!")
        val fileName: String = "passwordinfo.ftl"
        val params = Maps.newHashMap[String, AnyRef]
        params.put("user", user)
        mimeMessage.setText(createMailTemplate(fileName, params), "utf-8", "html")
      }
    }
    mailSender.send(preparator)
  }

  @Async def sendNewTagInformation(tag: Tag) {
    val preparator: MimeMessagePreparator = new MimeMessagePreparator() {
      @throws(classOf[MessagingException])
      def prepare(mimeMessage: MimeMessage) {
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(MailService.DEFAULT_ADMIN_EMAIL))
        mimeMessage.setFrom(new InternetAddress(MailService.DEFAULT_ADMIN_EMAIL))
        mimeMessage.setSubject("태그 추가 요청합니다.")
        val fileName: String = "requesttaginfo.ftl"
        val params = Maps.newHashMap[String, AnyRef]
        params.put("tag", tag)
        mimeMessage.setText(createMailTemplate(fileName, params), "utf-8", "html")
      }
    }
    mailSender.send(preparator)
  }

  private def createMailTemplate(fileName: String, params: Map[String, AnyRef]): String = {
    var template = configuration.getTemplate(fileName)
    val result = FreeMarkerTemplateUtils.processTemplateIntoString(template, params)
    log.debug("mail auth body : {}", result)
    result
  }
}
