package net.slipp.domain.qna;

import net.slipp.domain.user.SocialUser;

public class AnswerBuilder {
	private int totalLiked = 0;
	private SocialUser writer;
	
	public static AnswerBuilder anAnswer() {
		return new AnswerBuilder();
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
		answer.writedBy(writer);
		return answer;
	}
}
