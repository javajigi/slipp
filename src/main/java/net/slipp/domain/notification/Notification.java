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
	
	public Long getNotificationId() {
		return notificationId;
	}

	public Long getPostId() {
		return postId;
	}

	public Long getTargetSocialUserId() {
		return targetSocialUserId;
	}

	public boolean isReaded() {
		return readed;
	}
	
	public void read() {
		this.readed = true;
	}

	@Override
	public String toString() {
		return "Notification [postId=" + postId + ", targetSocialUserId=" + targetSocialUserId + ", readed=" + readed
				+ "]";
	}

}
