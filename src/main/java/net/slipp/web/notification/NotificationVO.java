package net.slipp.web.notification;

public class NotificationVO {
	private Long questionId;
	
	private String title;

    private boolean readed;
	
	public NotificationVO(Long questionId, String title, boolean readed) {
		this.questionId = questionId;
		this.title = title;
		this.readed = readed;
	}
	
	public Long getQuestionId() {
		return questionId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public boolean isReaded() {
        return readed;
    }

	@Override
	public String toString() {
		return "NotificationVO [questionId=" + questionId + ", title=" + title + "]";
	}
}
