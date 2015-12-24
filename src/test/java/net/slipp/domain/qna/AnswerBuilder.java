package net.slipp.domain.qna;

import net.slipp.domain.user.SocialUser;

public class AnswerBuilder {
	private Long answerId = 0L;
	private int totalLiked = 0;
	private SocialUser writer;

	private AnswerBuilder() {}

	private AnswerBuilder(Long answerId) {
		this.answerId = answerId;
	}
	
	public static AnswerBuilder anAnswer() {
		return new AnswerBuilder();
	}

	public static AnswerBuilder anAnswer(Long answerId) {
		return new AnswerBuilder(answerId);
	}
	
	public AnswerBuilder withTotalLiked(int totalLiked) {
		this.totalLiked = totalLiked;
		return this;
	}
	
	public AnswerBuilder with(SocialUser writer) {
		this.writer = writer;
		return this;
	}
	
	public Answer build() {
		Answer answer = new Answer() {
			@Override
			public Integer getSumLike() {
				return totalLiked;
			}
		};
		answer.setAnswerId(answerId);
		answer.writedBy(writer);
		return answer;
	}
}
