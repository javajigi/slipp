package net.slipp.domain.notification;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Notification implements Serializable {
	private static final long serialVersionUID = 1177450736436743878L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long notificationId;

	@Column
	private Long postId;

	@Column
	private Long targetSocialUserId;
	
	public Long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public Long getTargetSocialUserId() {
		return targetSocialUserId;
	}

	public void setTargetSocialUserId(Long targetSocialUserId) {
		this.targetSocialUserId = targetSocialUserId;
	}

	public boolean isReaded() {
		return readed;
	}

	public void setReaded(boolean readed) {
		this.readed = readed;
	}

	@Column
	private boolean readed = false;

	public Notification() {

	}

	public Notification(Long socialUserId, Long postId) {
		this.targetSocialUserId = socialUserId;
		this.postId = postId;
	}

	public static Notification create(Long targetUserId, Long postId) {
		return new Notification(targetUserId, postId);
	}

	@Override
	public String toString() {
		return "Notification [postId=" + postId + ", targetSocialUserId=" + targetSocialUserId + ", readed=" + readed
				+ "]";
	}

}
