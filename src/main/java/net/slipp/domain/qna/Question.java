package net.slipp.domain.qna;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.slipp.domain.ProviderType;
import net.slipp.domain.tag.Tag;
import net.slipp.domain.tag.Tags;
import net.slipp.domain.user.SocialUser;
import net.slipp.service.tag.TagHelper;
import net.slipp.support.jpa.CreatedDateEntityListener;
import net.slipp.support.jpa.HasCreatedDate;
import net.slipp.support.wiki.SlippWikiUtils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.security.access.AccessDeniedException;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@Entity
@EntityListeners({ CreatedDateEntityListener.class })
@Cache(region = "question", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Question implements HasCreatedDate {
    private static final Integer DEFAULT_BEST_ANSWER = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long questionId;

    @ManyToOne
    @JoinColumn(foreignKey=@ForeignKey(name = "fk_question_writer"))
    private SocialUser writer;

    @ManyToOne
    @JoinColumn(foreignKey=@ForeignKey(name = "fk_question_latest_participant"))
    private SocialUser latestParticipant;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
    	name = "question_content_holder", 
    	joinColumns = @JoinColumn(
    					name = "question_id", unique = true,
    					foreignKey = @ForeignKey(name="fk_question_content_holder_question_id")))
    @Lob
    @Column(name = "contents", nullable = false)
    private Collection<String> contentsHolder;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", nullable = false, updatable = false)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date", nullable = false)
    private Date updatedDate;

    @Column(name = "answer_count", nullable = false)
    private int answerCount = 0;

    @Column(name = "show_count", nullable = false)
    private int showCount = 0;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "question_tag", 
    	joinColumns = @JoinColumn(name = "question_id",
				foreignKey = @ForeignKey(name="fk_question_tag_question_id")), 
    	inverseJoinColumns = @JoinColumn(name = "tag_id", 
    			foreignKey = @ForeignKey(name="fk_question_tag_tag_id")))
    private Set<Tag> tags = Sets.newHashSet();

    @Column(name = "denormalized_tags", length = 100)
    private String denormalizedTags; // 역정규화한 태그를 저장
    
    @Column(name = "sum_like", nullable = true, columnDefinition="integer DEFAULT 0")
    private Integer sumLike = 0;    

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    @OrderBy("answerId ASC")
    private List<Answer> answers;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;
    
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "question_sns_connections", 
    	joinColumns = @JoinColumn(name = "question_id", 
    							foreignKey = @ForeignKey(name="fk_question_sns_connection_question_id")))
    private Collection<SnsConnection> snsConnetions = Sets.newHashSet();    
    
    public Question() {
    }

    public Question(SocialUser loginUser, String title, String contents, Set<Tag> pooledTags) {
        this(null, loginUser, title, contents, pooledTags);
    }

    public Question(Long id, SocialUser loginUser, String title, String contents, Set<Tag> newTags) {
        this.questionId = id;
        this.writer = loginUser;
        this.latestParticipant = loginUser;
        this.title = title;
        this.contentsHolder = Lists.newArrayList(contents);
        newTags(newTags);
        this.updatedDate = new Date();
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public int getAnswerCount() {
        return answerCount;
    }
    
    public int getSnsAnswerCount() {
        int snsAnswerCount = 0;
        for (SnsConnection each : snsConnetions) {
            snsAnswerCount += each.getSnsAnswerCount();
        }
        return snsAnswerCount;
    }

    public int getTotalAnswerCount() {
        return answerCount + getSnsAnswerCount();
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public String getDenormalizedTags() {
        return this.denormalizedTags;
    }

    private boolean isEmptyContentsHolder() {
        return contentsHolder == null || contentsHolder.isEmpty();
    }

    public String getContents() {
        if (isEmptyContentsHolder()) {
            return "";
        }

        return Iterables.getFirst(contentsHolder, "");
    }

    public Long getQuestionId() {
        return questionId;
    }

    public boolean isWritedBy(SocialUser socialUser) {
        return writer.isSameUser(socialUser);
    }

    public SocialUser getWriter() {
        return writer;
    }

    public SocialUser getLatestParticipant() {
        return this.latestParticipant;
    }

    public String getTitle() {
        return title;
    }

    public String getSummaryTitle() {
        return StringUtils.left(title, 8) + "...";
    }

    public int getShowCount() {
        return showCount;
    }
    
    public Integer getSumLike() {
        return sumLike;
    }

    public String getPlainTags() {
        String displayTags = "";
        for (Tag tag : getTags()) {
            displayTags += tag.getName() + " ";
        }
        return displayTags;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void delete(SocialUser loginUser) {
        if (!isWritedBy(loginUser)) {
            throw new AccessDeniedException(loginUser.getDisplayName() + " is not owner!");
        }
        this.deleted = true;
        detaggedTags(this.tags);
    }

    public void newAnswered(Answer answer) {
        this.answerCount += 1;
        this.latestParticipant = answer.getWriter();
        this.updatedDate = new Date();
    }

    public void deAnswered(Answer answer) {
        // @TODO Question과 Answer 관계가 현재는 분리되어 있다. 연결하는 방식으로 개선해야 함.
        this.answerCount -= 1;
        if (answerCount > 0) {
            Answer lastAnswer = Iterables.getLast(getAnswers());
            this.latestParticipant = lastAnswer.getWriter();
            this.updatedDate = lastAnswer.getCreatedDate();
        }
    }

    public void tagsToDenormalizedTags() {
        this.denormalizedTags = TagHelper.denormalizedTags(getTags());
    }

    public boolean hasTag(Tag tag) {
        return tags.contains(tag);
    }

    public void newTags(Set<Tag> newTags) {
        detaggedTags(tags);
        taggedTags(newTags);
        this.tags = newTags;
        tagsToDenormalizedTags();
    }
    
	public DifferenceTags differenceTags(Set<Tag> newTags) {
		Set<Tag> oldTags = Collections.unmodifiableSet(this.tags);
		return new DifferenceTags(oldTags, newTags);
	}
    
    static void detaggedTags(Set<Tag> originalTags) {
        for (Tag tag : originalTags) {
            tag.deTagged();
        }
    }

    static void taggedTags(Set<Tag> newTags) {
        for (Tag tag : newTags) {
            tag.tagged();
        }
    }
    
	public void taggedTag(Tag newTag) {
		newTag.tagged();
		tags.add(newTag);
		tagsToDenormalizedTags();
	}
	
	public void detaggedTag(Tag tag) {
		tag.deTagged();
		tags.remove(tag);
		tagsToDenormalizedTags();
	}

    public void update(SocialUser loginUser, String title, String contents, Set<Tag> newTags) {
        if (!isWritedBy(loginUser)) {
            throw new AccessDeniedException(loginUser.getDisplayName() + " is not owner!");
        }

        this.title = title;
        this.contentsHolder = Lists.newArrayList(contents);
        newTags(newTags);
        this.updatedDate = new Date();
        this.latestParticipant = getWriter();
    }
    
    public void updateContentsByAdmin(String contents) {
        this.contentsHolder = Lists.newArrayList(contents);
    }

    public Set<SocialUser> findNotificationUser(SocialUser loginUser) {
        Answers newAnswers = new Answers(this.answers);
        Set<SocialUser> notifierUsers = newAnswers.findFacebookAnswerers();
        notifierUsers.add(this.writer);
        return Sets.difference(notifierUsers, Sets.newHashSet(loginUser));
    }

    public SnsConnection connected(String postId) {
        return connected(postId, null);
    }
    
    public SnsConnection connected(String postId, String groupId) {
        SnsConnection snsConnection = new SnsConnection(ProviderType.valueOf(writer.getProviderId()), postId, groupId);
        snsConnetions.add(snsConnection);
        return snsConnection;
    }

    public Collection<SnsConnection> getSnsConnection() {
        return snsConnetions;
    }

    public boolean isSnsConnected() {
        return !snsConnetions.isEmpty();
    }
    
    /**
     * 베스트 댓글 하나를 반환한다.
     * 
     * @return
     */
    public Answer getBestAnswer() {
        if (CollectionUtils.isEmpty(getAnswers())) {
            return null;
        }

        Answer answer = getTopLikeAnswer();
        if (!answer.likedMoreThan(DEFAULT_BEST_ANSWER)) {
            return null;
        }

        return answer;
    }
    
    public void upRank() {
        this.sumLike += 1;
    }

    private Answer getTopLikeAnswer() {
        List<Answer> sortAnswers = Lists.newArrayList();
        sortAnswers.addAll(getAnswers());
        Collections.sort(sortAnswers);
        return sortAnswers.get(0);
    }
    
    public Set<Tag> getConnectedGroupTag() {
        return new Tags(tags).getConnectedGroupTags();
    }
    
	public void convertWiki() {
		String contents = SlippWikiUtils.convertWiki(getContents());
		this.contentsHolder = Lists.newArrayList(contents);
	}
    
    @Override
    public String toString() {
        return "Question [questionId=" + questionId + ", writer=" + writer + ", title=" + title + ", contentsHolder="
                + contentsHolder + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + ", answerCount="
                + answerCount + ", showCount=" + showCount + ", tags=" + tags + ", answers=" + answers + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + answerCount;
        result = prime * result + ((answers == null) ? 0 : answers.hashCode());
        result = prime * result + ((contentsHolder == null) ? 0 : contentsHolder.hashCode());
        result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
        result = prime * result + ((questionId == null) ? 0 : questionId.hashCode());
        result = prime * result + showCount;
        result = prime * result + ((tags == null) ? 0 : tags.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((updatedDate == null) ? 0 : updatedDate.hashCode());
        result = prime * result + ((writer == null) ? 0 : writer.hashCode());
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
        Question other = (Question) obj;
        if (answerCount != other.answerCount)
            return false;
        if (answers == null) {
            if (other.answers != null)
                return false;
        } else if (!answers.equals(other.answers))
            return false;
        if (contentsHolder == null) {
            if (other.contentsHolder != null)
                return false;
        } else if (!contentsHolder.equals(other.contentsHolder))
            return false;
        if (createdDate == null) {
            if (other.createdDate != null)
                return false;
        } else if (!createdDate.equals(other.createdDate))
            return false;
        if (questionId == null) {
            if (other.questionId != null)
                return false;
        } else if (!questionId.equals(other.questionId))
            return false;
        if (showCount != other.showCount)
            return false;
        if (tags == null) {
            if (other.tags != null)
                return false;
        } else if (!tags.equals(other.tags))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (updatedDate == null) {
            if (other.updatedDate != null)
                return false;
        } else if (!updatedDate.equals(other.updatedDate))
            return false;
        if (writer == null) {
            if (other.writer != null)
                return false;
        } else if (!writer.equals(other.writer))
            return false;
        return true;
    }
}
