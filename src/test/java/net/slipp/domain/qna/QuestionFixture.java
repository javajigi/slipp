package net.slipp.domain.qna;

public class QuestionFixture {
	public static Question createDto(String title, String contents, String plainTags) {
		Question question = new Question();
		question.setTitle(title);
		question.setContents(contents);
		question.setPlainTags(plainTags);
		return question;
	}
}
