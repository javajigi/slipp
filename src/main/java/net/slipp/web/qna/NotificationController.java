package net.slipp.web.qna;

import javax.annotation.Resource;

import net.slipp.domain.user.SocialUser;
import net.slipp.service.qna.NotificationService;
import net.slipp.support.web.argumentresolver.LoginUser;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/notifications")
public class NotificationController {
	@Resource(name = "notificationService")
	private NotificationService notificationService;
	
	@RequestMapping("/read")
	public @ResponseBody String read(@LoginUser SocialUser loginUser) {
		notificationService.readNotifications(loginUser);
		return "ok";
	}
}
