package net.slipp.domain.qna;

public enum ScoreLikeType {
	ANSWER, QUESTION;
	public static final String COLUMN_DEFINITION = "enum('ANSWER','QUESTION')";
}
