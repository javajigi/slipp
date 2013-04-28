package net.slipp.service;

import javax.annotation.Resource;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {
	@Resource(name = "mailSender")
	private MailSender mailSender;
	
    @Async
    public void send(SimpleMailMessage mailMessage) {
    	mailSender.send(mailMessage);
    }
}
