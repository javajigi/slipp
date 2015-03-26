package net.slipp.service.qna;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import net.slipp.domain.notification.Notification;
import net.slipp.domain.qna.Answer;
import net.slipp.domain.qna.Question;
import net.slipp.domain.user.SocialUser;
import net.slipp.repository.notification.NotificationRepository;
import net.slipp.repository.qna.AnswerRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.FacebookType;

@Service
@Transactional
public class NotificationService {
    @Value("#{applicationProperties['facebook.accessToken']}")
    private String accessToken;

    @Resource(name = "answerRepository")
    private AnswerRepository answerRepository;

    @Resource(name = "notificationRepository")
    private NotificationRepository notificationRepository;

    @Async
    public void notifyToFacebook(SocialUser loginUser, Long answerId) {
        Assert.notNull(answerId, "answerId should be not null!");
        
        Answer answer = answerRepository.findOne(answerId);
        Assert.notNull(answer, "Answer should be not null!");
        
        Question question = answer.getQuestion();
        Set<SocialUser> notifieeUsers = question.findNotificationUser(loginUser);

        if (notifieeUsers.isEmpty()) {
            return;
        }

        FacebookClient facebookClient = new DefaultFacebookClient(accessToken, Version.VERSION_2_2);
        for (SocialUser notifiee : notifieeUsers) {
            String uri = String.format("/%s/notifications", notifiee.getProviderUserId());
            String template = String.format("%s님이 \"%s\" 글에 답변을 달았습니다.", loginUser.getUserId(), question.getTitle());
            String href = String.format("/questions/%d#answer-%d", question.getQuestionId(), answer.getAnswerId());

            facebookClient.publish(uri, FacebookType.class, Parameter.with("template", template),
                    Parameter.with("href", href));
        }
    }

    @Async
    public void notifyToSlipp(SocialUser notifier, Long answerId) {
        Assert.notNull(answerId, "answerId should be not null!");

        Answer answer = answerRepository.findOne(answerId);
        Assert.notNull(answer, "Answer should be not null!");
        Question question = answer.getQuestion();
        Set<SocialUser> notifieeUsers = question.findNotificationUser(notifier);
        if (notifieeUsers.isEmpty()) {
            return;
        }

        for (SocialUser notifiee : notifieeUsers) {
            Notification notification = new Notification(notifier, notifiee, question);
            notificationRepository.save(notification);
        }
    }

    public long countByNotifiee(SocialUser notifiee) {
        Long count = notificationRepository.countByNotifiee(notifiee);
        if (count == null) {
            return 0L;
        }
        return count;
    }

    public List<Notification> findNotificationsAndReaded(SocialUser notifiee, Pageable pageable) {
        List<Notification> notifications = notificationRepository.findNotifications(notifiee, pageable);
        notificationRepository.updateReaded(notifiee);
        return notifications;
    }
}
