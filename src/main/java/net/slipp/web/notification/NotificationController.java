package net.slipp.web.notification;

import java.util.List;

import javax.annotation.Resource;

import net.slipp.domain.notification.Notification;
import net.slipp.domain.qna.Question;
import net.slipp.domain.user.SocialUser;
import net.slipp.service.qna.NotificationService;
import net.slipp.support.web.argumentresolver.LoginUser;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

@Controller
@RequestMapping("/notifications")
public class NotificationController {
	private static final int DEFAULT_PAGE_NO = 0;
	private static final int DEFAULT_PAGE_SIZE = 5;
	
    @Resource (name = "notificationService")
    private NotificationService notificationService; 
	
	@RequestMapping("")
	public @ResponseBody List<NotificationVO> list(@LoginUser SocialUser notifiee) {
		Pageable pageable = new PageRequest(DEFAULT_PAGE_NO, DEFAULT_PAGE_SIZE, new Sort(Direction.DESC, "notificationId"));
		
		List<NotificationVO> notificationVOs = Lists.newArrayList();
		List<Notification> notifications = notificationService.findNotificationsAndReaded(notifiee, pageable);
		for (Notification notification : notifications) {
			Question question = notification.getQuestion();
			notificationVOs.add(new NotificationVO(question.getQuestionId(), question.getTitle(), notification.isReaded()));
		}
		
		return notificationVOs;
	}
}
