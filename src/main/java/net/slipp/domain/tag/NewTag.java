package net.slipp.domain.tag;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import net.slipp.domain.qna.Question;
import net.slipp.domain.user.SocialUser;

import com.google.common.collect.Sets;

@Entity
public class NewTag {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long tagId;

	@Column(name = "name", length = 50, nullable = false)
	private String name;
	
	@Column(name = "deleted", nullable = false)
	private boolean deleted = false;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "newtag_user", joinColumns = @JoinColumn(name = "newtag_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	@org.hibernate.annotations.ForeignKey(name = "fk_newtag_user_newtag_id", inverseName = "fk_newtag_user_user_id")
	private Set<SocialUser> users;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "newtag_question", joinColumns = @JoinColumn(name = "newtag_id"), inverseJoinColumns = @JoinColumn(name = "question_id"))
	@org.hibernate.annotations.ForeignKey(name = "fk_newtag_question_newtag_id", inverseName = "fk_newtag_question_question_id")
	private Set<Question> questions;
	
	private int taggedCount = 1;
	
	public NewTag() {
	}
	
	public NewTag(String name) {
		this.name = name;
	}
	
	public NewTag(Long tagId, String name) {
		this.tagId = tagId;
		this.name = name;
	}
	
	public Long getTagId() {
		return tagId;
	}
	
	public String getName() {
		return name;
	}
	
	public void tagged() {
		taggedCount += 1;
	}
	
	public boolean isDeleted() {
		return deleted;
	}
	
	public void deleted() {
		deleted = true;
	}

	public int getTaggedCount() {
		return taggedCount;
	}
	
	public void addUser(SocialUser socialUser) {
		if (users == null) {
			users = Sets.newHashSet();
		}
		users.add(socialUser);
	}
	
	public Set<SocialUser> getUsers() {
		return users;
	}
	
	public void addQuestion(Question question) {
		if (questions == null) {
			questions = Sets.newHashSet();
		}
		
		questions.add(question);
	}
	
	public Set<Question> getQuestions() {
		return questions;
	}
	
	public Tag createTag(Tag parentTag) {
		return new Tag(getName(), parentTag);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((tagId == null) ? 0 : tagId.hashCode());
		result = prime * result + taggedCount;
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
		NewTag other = (NewTag) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (tagId == null) {
			if (other.tagId != null)
				return false;
		} else if (!tagId.equals(other.tagId))
			return false;
		if (taggedCount != other.taggedCount)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NewTag [tagId=" + tagId + ", name=" + name + ", taggedCount=" + taggedCount + "]";
	}
}
