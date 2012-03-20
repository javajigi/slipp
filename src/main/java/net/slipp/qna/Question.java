package net.slipp.qna;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

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
import javax.persistence.Transient;

import net.slipp.qna.repository.TagRepository;
import net.slipp.social.connect.SocialUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

@Entity
public class Question {
	private Logger logger = LoggerFactory.getLogger(Question.class);
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long questionId;

	@OneToOne
	@org.hibernate.annotations.ForeignKey(name = "fk_question_social_user_id")
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
	
	public Question() {
	}
	
	Question(SocialUser writer, String title, String contents, String plainTags) {
		this.writer = writer;
		this.title = title;
		setContents(contents);
		this.plainTags = plainTags;
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

	public void setPalinTags(String plainTags) {
		this.plainTags = plainTags;
	}
	
	public void initializeTags(TagRepository tagRepository) {
		Set<String> parsedTags = parseTags();
		Set<Tag> newTags = loadTags(parsedTags, tagRepository);
		Set<Tag> originalTags = tags;
		this.tags = newTags;
		addNewTags(newTags, originalTags);
		removeTags(newTags, originalTags);
	}

	private void removeTags(Set<Tag> newTags, Set<Tag> orginalTags) {
		SetView<Tag> removedTags = Sets.difference(orginalTags, newTags);
		logger.debug("removedTags : {}", removedTags);
		for (Tag tag : removedTags) {
			tag.deTagged();
		}
	}

	private void addNewTags(Set<Tag> newTags, Set<Tag> orginalTags) {
		SetView<Tag> addedTags = Sets.difference(newTags, orginalTags);
		logger.debug("addedTags : {}", addedTags);
		for (Tag tag : addedTags) {
			tag.tagged();
		}
	}
	
	private Set<String> parseTags() {
		Set<String> parsedTags = Sets.newHashSet();
		StringTokenizer tokenizer = new StringTokenizer(plainTags, " ");
		while (tokenizer.hasMoreTokens()) {
			parsedTags.add(tokenizer.nextToken());
		}
		return parsedTags;
	}

	private Set<Tag> loadTags(Set<String> parsedTags, TagRepository tagRepository) {
		Set<Tag> tags = Sets.newHashSet();
		for (String parsedTag : parsedTags) {
			tags.add(tagRepository.findByName(parsedTag));
		}
		return tags;
	}
}
