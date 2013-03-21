package net.slipp.domain.qna;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class SnsConnection {
	@Enumerated(EnumType.STRING)
	@Column(name = "sns_type", nullable = true, columnDefinition = SnsType.COLUMN_DEFINITION)
	private SnsType snsType;
	
	@Column(name = "post_id", length = 100, nullable = true)
	private String postId;

	public SnsConnection() {
	}

	public SnsConnection(SnsType snsType, String postId) {
		this.snsType = snsType;
		this.postId = postId;
	}
	
	public boolean isConnected() {
		return !StringUtils.isBlank(postId);
	}
	
	public SnsType getSnsType() {
		return snsType;
	}
	
	public String getPostId() {
		return postId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((postId == null) ? 0 : postId.hashCode());
		result = prime * result + ((snsType == null) ? 0 : snsType.hashCode());
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
		SnsConnection other = (SnsConnection) obj;
		if (postId == null) {
			if (other.postId != null)
				return false;
		} else if (!postId.equals(other.postId))
			return false;
		if (snsType != other.snsType)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SnsConnection [snsType=" + snsType + ", postId=" + postId + "]";
	}
}
