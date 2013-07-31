package net.slipp.domain.qna;

import java.util.Date;

import com.restfb.types.CategorizedFacebookType;
import com.restfb.types.Comment;

public class FacebookComment {
    private String id;
    private String userId;
    private String name;
    private Date createdTime;
    private String message;

    private FacebookComment(String id, String userId, String name, Date createdTime, String message) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.createdTime = createdTime;
        this.message = message;
    }

    public static FacebookComment create(Comment comment) {
        CategorizedFacebookType user = comment.getFrom();
        return new FacebookComment(comment.getId(), user.getId(), user.getName(), comment.getCreatedTime(), comment.getMessage());
    }

    public String getId() {
        return id;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public String getMessage() {
        return message;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "FacebookComment [id=" + id + ", userId=" + userId + ", name=" + name + ", createdTime=" + createdTime
                + ", message=" + message + "]";
    }
}
