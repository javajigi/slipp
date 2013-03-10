package net.slipp.repository.notification;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;

import net.slipp.domain.notification.Notification;
import net.slipp.domain.qna.Question;
import net.slipp.domain.tag.Tag;
import net.slipp.domain.user.SocialUser;
import net.slipp.domain.user.SocialUserBuilder;
import net.slipp.repository.qna.QuestionRepository;
import net.slipp.repository.user.SocialUserRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/test-applicationContext.xml")
public class NotificationRepositoryIT {
	@Autowired
	private NotificationRepository notificationRepository;
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private SocialUserRepository socialUserRepository;
	
	@Test
	@Transactional
	public void notification_lifecycle() throws Exception {
		// given
		SocialUser notifiee = SocialUserBuilder.aSocialUser().createTestUser("javajigi");
		notifiee = socialUserRepository.save(notifiee);
		SocialUser notifier = SocialUserBuilder.aSocialUser().createTestUser("sanjigi");
		notifier = socialUserRepository.save(notifier);
		Question question = new Question(notifiee, "this is title", "this is contents", new HashSet<Tag>());
//		Question question = QuestionBuilder.aQuestion()
//				.withTitle("this is title")
//				.withContents("this is contents")
//				.withWriter(writer)
//				.build();
		question = questionRepository.save(question);
		
		Notification notification = new Notification(notifier, notifiee, question);
		notificationRepository.save(notification);
		
		// when
		Pageable pageable = new PageRequest(0, 5, new Sort(Direction.DESC, "notificationId"));
		List<Notification> notifications = notificationRepository.findNotifications(notifiee, pageable);
		assertThat(notifications.size(), is(1));
		
		notificationRepository.updateReaded(notifiee);
		notifications = notificationRepository.findNotifications(notifiee, pageable);
		assertThat(notifications.size(), is(0));
	}
}
