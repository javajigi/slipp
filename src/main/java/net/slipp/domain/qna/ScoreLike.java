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

    @Column(name = "liked")
    private boolean liked;

    public ScoreLike() {
    }
    
    private ScoreLike(ScoreLikeType scoreLikeType, Long socialUserId, Long targetId, boolean liked) {
        this.scoreLikeType = scoreLikeType;
        this.socialUserId = socialUserId;
        this.targetId = targetId;
        this.liked = liked;
    }

    public Long getId() {
        return id;
    }

    public Long getSocialUserId() {
        return socialUserId;
    }

    public ScoreLikeType getScoreLikeType() {
        return scoreLikeType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public boolean isLiked() {
        return liked;
    }
    
    public static ScoreLike createLikedScoreLike(ScoreLikeType scoreLikeType, Long socialUserId, Long targetId) {
        return new ScoreLike(scoreLikeType, socialUserId, targetId, true);
    }
    
    public static ScoreLike createDisLikedScoreLike(ScoreLikeType scoreLikeType, Long socialUserId, Long targetId) {
        return new ScoreLike(scoreLikeType, socialUserId, targetId, false);
    }

    @Override
    public String toString() {
        return "ScoreLike [id=" + id + ", scoreLikeType=" + scoreLikeType + ", socialUserId=" + socialUserId
                + ", targetId=" + targetId + ", liked=" + liked + "]";
    }
}
