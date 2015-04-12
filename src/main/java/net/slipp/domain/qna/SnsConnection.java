package net.slipp.domain.qna;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import net.slipp.domain.ProviderType;

import org.apache.commons.lang3.StringUtils;

@Embeddable
public class SnsConnection {
    @Enumerated(EnumType.STRING)
    @Column(name = "sns_type", nullable = true, columnDefinition = ProviderType.COLUMN_DEFINITION)
    private ProviderType snsType;

    @Column(name = "post_id", length = 100, nullable = true)
    private String postId;
    
    @Column(name = "group_id", length = 100, nullable = true)
    private String groupId;

    @Column(name = "sns_answer_count", nullable = false)
    private int snsAnswerCount = 0;

    public SnsConnection() {
    }

    public SnsConnection(ProviderType snsType, String postId) {
        this(snsType, postId, null);
    }
    
    public SnsConnection(ProviderType snsType, String postId, String groupId) {
        this.snsType = snsType;
        this.postId = removeId(postId);
        this.groupId = groupId;
    }

    public boolean isConnected() {
        return !StringUtils.isBlank(postId);
    }
    
    public boolean isGroupConnected() {
        return !StringUtils.isBlank(groupId);
    }

    public ProviderType getSnsType() {
        return snsType;
    }

    public String getPostId() {
        return postId;
    }
    
    public String getGroupId() {
		return groupId;
	}

    public int getSnsAnswerCount() {
        return snsAnswerCount;
    }

    public void updateAnswerCount(int answerCount) {
        this.snsAnswerCount = answerCount;
    }
    
    static String removeId(String postId) {
    	if (StringUtils.isBlank(postId)) {
    		return postId;
    	}
    	
    	if (!postId.contains("_")) {
    		return postId;
    	}
    	
    	String[] postIds = postId.split("_");
    	
    	if (postIds.length != 2) {
    		return postId;
    	}
    	
    	return postIds[1];
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
        return "SnsConnection [snsType=" + snsType + ", postId=" + postId + ", snsAnswerCount=" + snsAnswerCount + "]";
    }
}
