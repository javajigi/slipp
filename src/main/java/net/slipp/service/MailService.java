package net.slipp.service;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import net.slipp.domain.tag.Tag;
import net.slipp.domain.user.SocialUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.google.common.collect.Maps;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Service
public class MailService {
    private static Logger log = LoggerFactory.getLogger(MailService.class);
    
    private static final String DEFAULT_ADMIN_EMAIL = "javajigi@gmail.com";

    @Resource(name = "mailSender")
    JavaMailSender mailSender;

    @Resource(name = "freemarkerConfiguration")
    Configuration configuration;

    @Async
    public void sendPasswordInformation(final SocialUser user) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws MessagingException {
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
                mimeMessage.setFrom(new InternetAddress(DEFAULT_ADMIN_EMAIL));
                mimeMessage.setSubject("SLiPP에 오신것을 환영합니다!");

                String fileName = "passwordinfo.ftl";
                Map<String, Object> params = Maps.newHashMap();
                params.put("user", user);
                mimeMessage.setText(createMailTemplate(fileName, params), "utf-8", "html");
            }
        };
        mailSender.send(preparator);
    }
    
    @Async
    public void sendNewTagInformation(final Tag tag) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws MessagingException {
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(DEFAULT_ADMIN_EMAIL));
                mimeMessage.setFrom(new InternetAddress(DEFAULT_ADMIN_EMAIL));
                mimeMessage.setSubject("태그 추가 요청합니다.");

                String fileName = "requesttaginfo.ftl";
                Map<String, Object> params = Maps.newHashMap();
                params.put("tag", tag);
                mimeMessage.setText(createMailTemplate(fileName, params), "utf-8", "html");
            }
        };
        mailSender.send(preparator);        
    }

    private String createMailTemplate(String fileName, Map<String, Object> params) {
        Template template;
        try {
            template = configuration.getTemplate(fileName);
        } catch (IOException e) {
            log.error(e.toString());
            return null;
        }

        try {
            String result = FreeMarkerTemplateUtils.processTemplateIntoString(template, params);
            log.debug("mail auth body : {}", result);
            return result;
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }
    }
}
