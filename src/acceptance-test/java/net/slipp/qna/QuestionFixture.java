package net.slipp.qna;


public class QuestionFixture {
	private String title;
	private String contents;
	private String plainTags;

	public QuestionFixture() {
		this.title = "DEFAULT - 지속 가능한 삶, 프로그래밍, 프로그래머";
		this.contents = "DEFAULT - 이 공간은 삶과 일의 균형을 맞추면서 지속 가능한 삶을 살아갈 것인가에 고민을 담기 위한 곳이다.";
		this.plainTags = "java javascript";
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getPlainTags() {
		return plainTags;
	}

	public void setPlainTags(String plainTags) {
		this.plainTags = plainTags;
	}
}
