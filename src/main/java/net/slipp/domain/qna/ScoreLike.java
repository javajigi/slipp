package net.slipp.domain.qna;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "score_like")
public class ScoreLike {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "like_type", nullable = false, columnDefinition = ScoreLikeType.COLUMN_DEFINITION)
	private ScoreLikeType scoreLikeType;
	
	@Column(name = "social_user_id")
	private Long socialUserId;
	
	@Column(name = "target_id")
	private Long targetId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSocialUserId() {
		return socialUserId;
	}

	public void setSocialUserId(Long socialUserId) {
		this.socialUserId = socialUserId;
	}

	public ScoreLikeType getScoreLikeType() {
		return scoreLikeType;
	}

	public void setScoreLikeType(ScoreLikeType scoreLikeType) {
		this.scoreLikeType = scoreLikeType;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public ScoreLike() {
	}
	
	public ScoreLike(ScoreLikeType scoreLikeType, Long socialUserId, Long targetId) {
		this.scoreLikeType = scoreLikeType;
		this.socialUserId = socialUserId;
		this.targetId = targetId;
	}
}
