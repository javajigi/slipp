package net.slipp.domain.tag;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.slipp.support.jpa.CreatedDateEntityListener;
import net.slipp.support.jpa.HasCreatedDate;

@Entity
@Table(indexes = {
	@Index(name = "idx_tagged_history_tag", columnList="tag_id"),
	@Index(name = "idx_tagged_history_question", columnList="question_id")}
)
@EntityListeners({ CreatedDateEntityListener.class })
public class TaggedHistory implements HasCreatedDate {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long historyId;
	
	@Column(name = "tag_id", nullable = false, updatable = false)
	private Long tagId;
	
	@Column(name = "question_id", nullable = false, updatable = false)
	private Long questionId;
	
	@Column(name = "user_id", nullable = false, updatable = false)
	private Long userId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tagged_type", nullable = false, updatable = false, columnDefinition = TaggedType.COLUMN_DEFINITION)
	private TaggedType taggedType;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date", nullable = false, updatable = false)
	private Date createdDate;
	
	public TaggedHistory(Long tagId, Long questionId, Long userId, TaggedType taggedType) {
		this.tagId = tagId;
		this.questionId = questionId;
		this.userId = userId;
		this.taggedType = taggedType;
	}

	@Override
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public Date getCreatedDate() {
		return this.createdDate;
	}
	
	public Long getTagId() {
		return tagId;
	}
	
	public Long getQuestionId() {
		return questionId;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public TaggedType getTaggedType() {
		return taggedType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + ((historyId == null) ? 0 : historyId.hashCode());
		result = prime * result + ((questionId == null) ? 0 : questionId.hashCode());
		result = prime * result + ((tagId == null) ? 0 : tagId.hashCode());
		result = prime * result + ((taggedType == null) ? 0 : taggedType.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaggedHistory other = (TaggedHistory) obj;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (historyId == null) {
			if (other.historyId != null)
				return false;
		} else if (!historyId.equals(other.historyId))
			return false;
		if (questionId == null) {
			if (other.questionId != null)
				return false;
		} else if (!questionId.equals(other.questionId))
			return false;
		if (tagId == null) {
			if (other.tagId != null)
				return false;
		} else if (!tagId.equals(other.tagId))
			return false;
		if (taggedType != other.taggedType)
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TaggedHistory [historyId=" + historyId + ", tagId=" + tagId + ", questionId=" + questionId
				+ ", userId=" + userId + ", taggedType=" + taggedType + ", createdDate=" + createdDate + "]";
	}
}
