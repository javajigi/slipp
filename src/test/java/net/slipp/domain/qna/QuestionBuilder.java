package net.slipp.domain.qna;

import java.util.List;
import java.util.Set;

import net.slipp.domain.tag.Tag;
import net.slipp.domain.user.SocialUser;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class QuestionBuilder {
	private Long questionId;
	private SocialUser writer;
	private String title;
	private String contents;
	private Set<Tag> tags = Sets.newHashSet();
	private List<Answer> answers = Lists.newArrayList();

	private QuestionBuilder() {}

	private QuestionBuilder(Long questionId) {
		this.questionId = questionId;
	}
	
	public static QuestionBuilder aQuestion() {
		return new QuestionBuilder();
	}

	public static QuestionBuilder aQuestion(Long questionId) {
		return new QuestionBuilder(questionId);
	}
	
	public QuestionBuilder withWriter(SocialUser writer) {
		this.writer = writer;
		return this;
	}
	
	public QuestionBuilder withTitle(String title) {
		this.title = title;
		return this;
	}
	
	public QuestionBuilder withContents(String contents) {
		this.contents = contents;
		return this;
	}
	
	public QuestionBuilder withTag(Tag tag) {
		tags.add(tag);
		return this;
	}
	
	public QuestionBuilder withAnswer(Answer answer) {
		answers.add(answer);
		return this;
	}
	
	public Question build() {
		Question question = new Question(questionId, writer, title, contents, tags) {
			public List<Answer> getAnswers() {
				return answers;
			}
		};
		for (Answer answer : answers) {
			question.newAnswered(answer);
		}
		
		return question;
	}
}
