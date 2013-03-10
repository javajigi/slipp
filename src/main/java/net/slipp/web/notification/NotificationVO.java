package net.slipp.web.notification;

public class NotificationVO {
	private Long questionId;
	
	private String title;
	
	public NotificationVO(Long questionId, String title) {
		this.questionId = questionId;
		this.title = title;
	}
	
	public Long getQuestionId() {
		return questionId;
	}
	
	public String getTitle() {
		return title;
	}

	@Override
	public String toString() {
		return "NotificationVO [questionId=" + questionId + ", title=" + title + "]";
	}
}
