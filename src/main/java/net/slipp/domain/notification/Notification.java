package net.slipp.domain.notification;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import net.slipp.domain.qna.Question;
import net.slipp.domain.user.SocialUser;

@Entity
public class Notification implements Serializable {
	private static final long serialVersionUID = 1177450736436743878L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long notificationId;

    @OneToOne
    @JoinColumn(foreignKey=@ForeignKey(name = "fk_notification_notifier"))
	private SocialUser notifier;
	
    @OneToOne
    @JoinColumn(foreignKey=@ForeignKey(name = "fk_notification_notifiee"))
	private SocialUser notifiee;
    
    @OneToOne
    @JoinColumn(foreignKey=@ForeignKey(name = "fk_notification_question"))
	private Question question;

    @Column(name = "readed", nullable=false)
	private boolean readed = false;

	public Notification() {
	}
	
	public Notification(SocialUser notifier, SocialUser notifiee, Question question) {
		this.notifier = notifier;
		this.notifiee = notifiee;
		this.question = question;
	}

	public Long getNotificationId() {
		return notificationId;
	}

	public SocialUser getNotifier() {
		return notifier;
	}

	public SocialUser getNotifiee() {
		return notifiee;
	}

	public Question getQuestion() {
		return question;
	}

	public boolean isReaded() {
		return readed;
	}
}
