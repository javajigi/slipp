package net.slipp.web;

import javax.annotation.Resource;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/users")
public class UsersController {
	@Resource(name = "mailSender")
	private MailSender mailSender;
	
    @RequestMapping("/login")
    public String login() {
        return "users/login";
    }
    
    @RequestMapping("/form")
    public String form(Model model) {
    	model.addAttribute("user", new UserForm());
        return "users/form";
    }
    
    @RequestMapping(value="", method=RequestMethod.POST)
    public String create(UserForm user) {
    	SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("javajigi@gmail.com");
		message.setTo(user.getEmail());
		message.setSubject("SLiPP 회원가입 감사합니다.");
		message.setText("비밀번호는 aaaa입니다. 빨리 와서 변경해 주세요.");
		mailSender.send(message);
        return "redirect:/";
    }    
    
    @RequestMapping("/fblogout")
    public String logout() {
        return "users/fblogout";
    }
}
