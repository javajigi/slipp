package net.slipp.service;

import net.slipp.domain.user.SocialUser;
import net.slipp.domain.user.SocialUserBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import freemarker.template.Configuration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationContext-mail.xml")
public class MailServiceTest {
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private Configuration configuration;

    @Test
    public void sendPasswordInformation() {
        MailService dut = new MailService();
        dut.mailSender = mailSender;
        dut.configuration = configuration;
        
        SocialUser socialUser = new SocialUserBuilder()
            .withUserId("userId")
            .withEmail("javajigi@gmail.com")
            .withRawPassword("rawpassword")
            .build();
        dut.sendPasswordInformation(socialUser);
    }

}
