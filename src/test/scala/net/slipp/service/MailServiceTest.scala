package net.slipp.service

import net.slipp.domain.user.SocialUser
import net.slipp.domain.user.SocialUserBuilder
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import freemarker.template.Configuration

@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(Array("classpath:test-applicationContext-mail.xml")) class MailServiceTest {
  @Autowired private var mailSender: JavaMailSender = null
  @Autowired private var configuration: Configuration = null

  @Test def sendPasswordInformation {
    val dut: MailService = new MailService
    dut.mailSender = mailSender
    dut.configuration = configuration
    val socialUser: SocialUser = new SocialUserBuilder().withUserId("userId").withEmail("javajigi@gmail.com").withRawPassword("rawpassword").build
    dut.sendPasswordInformation(socialUser)
  }
}
