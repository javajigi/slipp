package net.slipp.repository.notification;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/test-applicationContext.xml")
public class NotificationRepositoryIT {
	private static final Logger log = LoggerFactory.getLogger(NotificationRepositoryIT.class);
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private SocialUserRepository socialUserRepository;
	
	private StopWatch stopWatch = new StopWatch();
	
	@Test
	@Transactional
	public void notification_lifecycle() throws Exception {
		// given
		SocialUser notifiee = SocialUserBuilder.aSocialUser().createTestUser("javajigi");
		notifiee = socialUserRepository.save(notifiee);
		SocialUser notifier = SocialUserBuilder.aSocialUser().createTestUser("sanjigi");
		notifier = socialUserRepository.save(notifier);
		Question question = new Question(notifiee, "this is title", "this is contents", new HashSet<Tag>());
		question = questionRepository.save(question);
		
		Notification notification = new Notification(notifier, notifiee, question);
		notificationRepository.save(notification);
		
		// when
		Pageable pageable = new PageRequest(0, 5, new Sort(Direction.DESC, "notificationId"));
		stopWatch.start();
		List<Notification> notifications = notificationRepository.findNotifications(notifiee, pageable);
		assertThat(notifications.size(), is(1));
		stopWatch.stop();
		log.debug("time to create: {}", stopWatch.getLastTaskTimeMillis() + "ms");
		
		Long count = notificationRepository.countByNotifiee(notifiee);
		assertThat(count, is(1L));
		
		notificationRepository.updateReaded(notifiee);
		notifications = notificationRepository.findNotifications(notifiee, pageable);
		assertThat(notificationRepository.countByNotifiee(notifiee), is(nullValue()));
	}
}
