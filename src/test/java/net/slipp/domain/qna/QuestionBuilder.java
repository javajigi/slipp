package net.slipp.domain.qna;

import net.slipp.domain.user.SocialUser;

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
		return new Question(writer, title, contents, tags);
	}
}
