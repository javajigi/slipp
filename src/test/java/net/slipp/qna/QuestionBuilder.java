package net.slipp.qna;

import net.slipp.social.connect.SocialUser;

public class QuestionBuilder {
	private SocialUser writer;
	
	private String title;
	
	private String contents;
	
	private String tags;
	
	public QuestionBuilder writer(SocialUser writer) {
		this.writer = writer;
		return this;
	}
	
	public QuestionBuilder title(String title) {
		this.title = title;
		return this;
	}
	
	public QuestionBuilder contents(String contents) {
		this.contents = contents;
		return this;
	}
	
	public QuestionBuilder tags(String tags) {
		this.tags = tags;
		return this;
	}
	
	public Question build() {
		return Question.create(writer, title, contents, tags);
	}
}
