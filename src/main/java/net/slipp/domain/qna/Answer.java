package net.slipp.domain.qna;

import java.util.Collection;
import java.util.Date;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.slipp.domain.ProviderType;
import net.slipp.domain.user.SocialUser;
import net.slipp.support.jpa.CreatedAndUpdatedDateEntityListener;
import net.slipp.support.jpa.HasCreatedAndUpdatedDate;
import net.slipp.support.wiki.SlippWikiUtils;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@Entity
@EntityListeners({ CreatedAndUpdatedDateEntityListener.class })
public class Answer implements HasCreatedAndUpdatedDate, Comparable<Answer> {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long answerId;
	
	@ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name = "fk_answer_writer"))
	private SocialUser writer;
	
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "answer_content_holder", 
		joinColumns = @JoinColumn(
							name = "answer_id", unique = true, 
	    					foreignKey = @ForeignKey(name="fk_answer_content_holder_answer_id")))
	@Lob
	@Column(name = "contents", nullable = false)
	private Collection<String> contentsHolder;
	
	@Transient
	private boolean connected;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date", nullable = false, updatable = false)
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_date", nullable = false)
	private Date updatedDate;
	
	@Column(name = "sum_like", nullable = true, columnDefinition="integer DEFAULT 0")
	private Integer sumLike = 0;
	
    @Column(name = "sum_dislike", nullable = true, columnDefinition="integer DEFAULT 0")
    private Integer sumDislike = 0;	
	
	@ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name = "fk_answer_parent_id"))
	private Question question;
	
    @Embedded
    private SnsConnection snsConnection = new SnsConnection();
	
	public Answer() {
	}

	public Answer(Long answerId) {
		this.answerId = answerId;
	}
	
	public Answer(String contents) {
	    setContents(contents);
	}

	public Question getQuestion() {
		return this.question;
	}

	public Long getAnswerId() {
		return answerId;
	}
	
	public void setAnswerId(Long answerId) {
		this.answerId = answerId;
	}

	public SocialUser getWriter() {
		return writer;
	}

	public void setContents(String newContents) {
		if (isEmptyContentsHolder()) {
			contentsHolder = Lists.newArrayList(newContents);
		} else {
			contentsHolder.clear();
			contentsHolder.add(newContents);
		}
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

	public void answerTo(Question question) {
		this.question = question;
		question.newAnswered(this);
	}

	public void writedBy(SocialUser user) {
		this.writer = user;		
	}
	
	public boolean isWritedBy(SocialUser loginUser) {
		return writer.isSameUser(loginUser);
	}
	
	public boolean isFacebookWriter() {
		return writer.isFacebookUser();
	}
	
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	public void updateAnswer(Answer answerDto) {
		this.contentsHolder = answerDto.contentsHolder;
	}
	
	public Integer getSumLike() {
		return sumLike;
	}
	
	public Integer getSumDislike() {
        return sumDislike;
    }

	public void upRank() {
		this.sumLike += 1;
	}
	
    public void downRank() {
        this.sumDislike += 1;
    }
	
	boolean likedMoreThan(int totalLiked) {
		if (getSumLike() >= totalLiked) {
			return true;
		}
		return false;
	}
	
    public SnsConnection connected(String postId) {
        this.snsConnection = new SnsConnection(ProviderType.valueOf(writer.getProviderId()), postId); 
        return this.snsConnection;
    }
    
	public void convertWiki() {
		String contents = SlippWikiUtils.convertWiki(getContents());
		this.contentsHolder = Lists.newArrayList(contents);		
	}
    
	@Override
	public int compareTo(Answer o) {
		return o.getSumLike().compareTo(getSumLike());
	}

	@Override
	public String toString() {
		return "Answer [answerId=" + answerId + ", writer=" + writer + ", contentsHolder=" + contentsHolder
				+ ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + ", question=" + question + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((answerId == null) ? 0 : answerId.hashCode());
		result = prime * result + ((contentsHolder == null) ? 0 : contentsHolder.hashCode());
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + ((question == null) ? 0 : question.hashCode());
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
		Answer other = (Answer) obj;
		if (answerId == null) {
			if (other.answerId != null)
				return false;
		} else if (!answerId.equals(other.answerId))
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
		if (question == null) {
			if (other.question != null)
				return false;
		} else if (!question.equals(other.question))
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
