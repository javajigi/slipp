package net.slipp.domain.qna;

import java.util.Collection;
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
import javax.persistence.Transient;

import net.slipp.domain.tag.Tag;
import net.slipp.domain.tag.TagService;
import net.slipp.domain.user.SocialUser;
import net.slipp.support.jpa.CreatedAndUpdatedDateEntityListener;
import net.slipp.support.jpa.HasCreatedAndUpdatedDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

@Entity
@EntityListeners({ CreatedAndUpdatedDateEntityListener.class })
public class Question implements HasCreatedAndUpdatedDate {
	private static final Logger logger = LoggerFactory.getLogger(Question.class);
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long questionId;
	
	@ManyToOne
	@org.hibernate.annotations.ForeignKey(name = "fk_question_writer")
	private SocialUser writer;
	
	@Column(name = "title", length=100, nullable = false)
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
	
	@Transient
	private String plainTags;

	@OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
	@OrderBy("answerId DESC")
	private List<Answer> answers;
	
	@Column(name = "deleted", nullable = false)
	private boolean deleted = false;
	
	public Question() {
	}
	
	public Question(Long id) {
		this.questionId = id;
	}
	
	Question(SocialUser writer, String title, String contents, String plainTags) {
		this.writer = writer;
		this.title = title;
		setContents(contents);
		this.plainTags = plainTags;
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

	public void initializeTags(TagService tagProcessor) {
		Set<Tag> newTags = tagProcessor.processTags(plainTags);
		Set<Tag> originalTags = tags;
		this.tags = newTags;
		addNewTags(newTags, originalTags);
		removeTags(newTags, originalTags);
	}

	private void removeTags(Set<Tag> newTags, Set<Tag> orginalTags) {
		SetView<Tag> removedTags = Sets.difference(orginalTags, newTags);
		logger.debug("removedTags size : {}", removedTags.size());
		for (Tag tag : removedTags) {
			tag.deTagged();
		}
	}

	private void addNewTags(Set<Tag> newTags, Set<Tag> orginalTags) {
		SetView<Tag> addedTags = Sets.difference(newTags, orginalTags);
		logger.debug("addedTags size : {}", addedTags.size());
		for (Tag tag : addedTags) {
			tag.tagged();
		}
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}
	
	public void writedBy(SocialUser user) {
		this.writer = user;
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

	public void setTitle(String title) {
		this.title = title;
	}

	public int getShowCount() {
		return showCount;
	}

	public void setShowCount(int showCount) {
		this.showCount = showCount;
	}

	public String getPlainTags() {
		String displayTags = "";
		for (Tag tag : this.tags) {
			displayTags += tag.getName() + " ";
		}
		return displayTags;
	}

	public void setPlainTags(String plainTags) {
		this.plainTags = plainTags;
	}

	public void setAnswerCount(int answerCount) {
		this.answerCount = answerCount;
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
	
	public void update(Question newQuestion) {
		this.title = newQuestion.title;
		this.contentsHolder = newQuestion.contentsHolder;
		this.plainTags = newQuestion.plainTags;
	}
	
	public void delete() {
		this.deleted = true;
	}
	
	public void increaseAnswerCount() {
		this.answerCount += 1;
	}
	
	public void decreaseAnswerCount() {
		this.answerCount -= 1;
	}
	
	@Override
	public String toString() {
		return "Question [questionId=" + questionId + ", writer=" + writer + ", title=" + title + ", contentsHolder="
				+ contentsHolder + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + ", answerCount="
				+ answerCount + ", showCount=" + showCount + ", tags=" + tags + ", plainTags=" + plainTags
				+ ", answers=" + answers + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + answerCount;
		result = prime * result + ((answers == null) ? 0 : answers.hashCode());
		result = prime * result + ((contentsHolder == null) ? 0 : contentsHolder.hashCode());
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + ((plainTags == null) ? 0 : plainTags.hashCode());
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
		if (plainTags == null) {
			if (other.plainTags != null)
				return false;
		} else if (!plainTags.equals(other.plainTags))
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
