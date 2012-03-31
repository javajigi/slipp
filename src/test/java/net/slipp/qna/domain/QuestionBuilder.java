package net.slipp.qna.domain;

import net.slipp.qna.domain.Question;



public class QuestionBuilder {
	private String writerId;
	
	private String writerName;
	
	private String title;
	
	private String contents;
	
	private String tags;
	
	public QuestionBuilder writerId(String writerId) {
		this.writerId = writerId;
		return this;
	}
	
	public QuestionBuilder writerName(String writerName) {
		this.writerName = writerName;
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
		return new Question(writerId, writerName, title, contents, tags);
	}
}
