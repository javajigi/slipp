package net.slipp.qna;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.slipp.social.connect.SocialUser;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@Entity
public class Question {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long questionId;

	@OneToOne
	@org.hibernate.annotations.ForeignKey(name = "fk_question_social_user_id")
	private SocialUser writer;

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
	private Set<Tag> tags;

	@OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
	@OrderBy("answerId DESC")
	private List<Answer> answers;
	
	public Question() {
	}
	
	public Question(SocialUser writer, String title, String contents) {
		this.writer = writer;
		this.title = title;
		setContents(contents);
	}

	public void addAnswer(Answer answer) {
		if (answers == null) {
			answers = Lists.newArrayList();
		}
		answers.add(answer);
		answerCount += 1;
		answer.setQuestion(this);
	}

	public List<Answer> getAnswers() {
		return answers;
	}
	
	public int getAnswerCount() {
		return answerCount;
	}
	
	public void addTag(Tag tag) {
		if (tags == null) {
			tags = Sets.newHashSet();
		}
		tags.add(tag);
		tag.tagged();
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

	public static Question create(SocialUser writer, String title, String contents) {
		return new Question(writer, title, contents);
	}
}
