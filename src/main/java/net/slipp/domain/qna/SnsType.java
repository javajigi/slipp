package net.slipp.domain.qna;

public enum SnsType {
	facebook,
	twitter,
	google;
	
	public static final String COLUMN_DEFINITION = "enum ('facebook', 'twitter', 'google')";
}
