package net.slipp.domain.fb;

import java.util.Date;

import net.slipp.domain.tag.Tag;
import net.slipp.support.web.tags.SlippFunctions;

import com.restfb.types.CategorizedFacebookType;
import com.restfb.types.Comment;

public class FacebookComment implements Comparable<FacebookComment> {
    private String id;
    private String userId;
    private String name;
    private Date createdTime;
    private String message;
    private String groupId;
    private String groupName;

    private FacebookComment(String id, String userId, String name, Date createdTime, String message, String groupId, String groupName) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.createdTime = createdTime;
        this.message = message;
        this.groupId = groupId;
        this.groupName = groupName;
    }

    public static FacebookComment create(Tag tag, Comment comment) {
        CategorizedFacebookType user = comment.getFrom();
        
        if (tag == null) {
        	return new FacebookComment(comment.getId(), user.getId(), user.getName(), comment.getCreatedTime(), comment.getMessage(), null, null);
        } 
        
        return new FacebookComment(comment.getId(), user.getId(), user.getName(), comment.getCreatedTime(), comment.getMessage(), tag.getGroupId(), tag.getName());
    }

    public String getId() {
        return id;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public String getMessage() {
        return SlippFunctions.hbr(message);
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }
    
    public String getGroupId() {
		return groupId;
	}
    
    public String getGroupName() {
		return groupName;
	}
    
    @Override
    public int compareTo(FacebookComment target) {
        return (int)(createdTime.getTime() - target.createdTime.getTime());
    }

    @Override
    public String toString() {
        return "FacebookComment [id=" + id + ", userId=" + userId + ", name=" + name + ", createdTime=" + createdTime
                + ", message=" + message + "]";
    }
}
