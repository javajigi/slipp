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

import net.slipp.domain.tag.Tag;
import net.slipp.domain.user.SocialUser;
import net.slipp.support.jpa.CreatedAndUpdatedDateEntityListener;
import net.slipp.support.jpa.HasCreatedAndUpdatedDate;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.access.AccessDeniedException;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@Entity
@EntityListeners({ CreatedAndUpdatedDateEntityListener.class })
public class Question implements HasCreatedAndUpdatedDate {
	private static final Integer DEFAULT_BEST_ANSWER = 2;
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long questionId;

    @ManyToOne
    @org.hibernate.annotations.ForeignKey(name = "fk_question_writer")
    private SocialUser writer;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "question_content_holder", joinColumns = @JoinColumn(name = "question_id", unique = true))
    @org.hibernate.annotations.ForeignKey(name = "fk_question_content_holder_question_id")
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
    @JoinTable(name = "question_tag", joinColumns = @JoinColumn(name = "question_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @org.hibernate.annotations.ForeignKey(name = "fk_question_tag_question_id", inverseName = "fk_question_tag_tag_id")
    private Set<Tag> tags = Sets.newHashSet();

    @Column(name = "denormalized_tags", length = 100)
    private String denormalizedTags; // 역정규화한 태그를 저장

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    @OrderBy("answerId ASC")
    private List<Answer> answers;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    public Question() {
    }

    public Question(SocialUser loginUser, String title, String contents, Set<Tag> pooledTags) {
        this(null, loginUser, title, contents, pooledTags);
    }
    
    public Question(Long id, SocialUser loginUser, String title, String contents, Set<Tag> newTags) {
    	this.questionId = id;
        this.writer = loginUser;
        this.title = title;
        this.contentsHolder = Lists.newArrayList(contents);
        newTags(newTags);
    }

	public List<Answer> getAnswers() {
        return answers;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public Set<Tag> getTags() {
        return tags;
    }
    
	public Set<Tag> getPooledTags() {
		Set<Tag> pooledTags = Sets.newHashSet();
		for (Tag tag : getTags()) {
			if (tag.isPooled()) {
				pooledTags.add(tag);
			}
		}
		return pooledTags;
	}

    public String getDenormalizedTags() {
        return this.denormalizedTags;
    }

    private String tagsToDenormalizedTags(Set<Tag> tags) {
        if (tags == null) {
            return null;
        }
        
        Function<Tag, String> tagToString = new Function<Tag, String>() {
            @Override
            public String apply(Tag input) {
                return input.getName();
            }
        };

        return Joiner.on(",").join(Collections2.transform(tags, tagToString));
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

    public String getTitle() {
        return title;
    }

    public int getShowCount() {
        return showCount;
    }

    public String getPlainTags() {
        String displayTags = "";
        for (Tag tag : getPooledTags()) {
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

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
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

    public void show() {
        this.showCount += 1;
    }

    public void newAnswered() {
        this.answerCount += 1;
    }

    public void deAnswered() {
        this.answerCount -= 1;
    }

	public void tagsToDenormalizedTags() {
		this.denormalizedTags = tagsToDenormalizedTags(getPooledTags());
	}
    
    public boolean hasTag(Tag tag) {
        return tags.contains(tag);
    }
    
    void newTags(Set<Tag> newTags) {
    	detaggedTags(tags);
    	taggedTags(newTags);
    	this.tags = newTags;
    	this.denormalizedTags = tagsToDenormalizedTags(getPooledTags());
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

    public void update(SocialUser loginUser, String title, String contents, Set<Tag> newTags) {
        if (!isWritedBy(loginUser)) {
            throw new AccessDeniedException(loginUser.getDisplayName() + " is not owner!");
        }
    	
        this.title = title;
        this.contentsHolder = Lists.newArrayList(contents);
        newTags(newTags);
    }

    public Set<SocialUser> findNotificationUser(SocialUser loginUser) {
        Answers newAnswers = new Answers(this.answers);
        Set<SocialUser> notifierUsers = newAnswers.findFacebookAnswerers();
        notifierUsers.add(this.writer);
        return Sets.difference(notifierUsers, Sets.newHashSet(loginUser));
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

    private Answer getTopLikeAnswer() {
        List<Answer> sortAnswers = Lists.newArrayList();
        sortAnswers.addAll(getAnswers());
        Collections.sort(sortAnswers);
        return sortAnswers.get(0);
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
