package net.slipp.domain.qna;

public class QuestionDto {
    private Long questionId;
    
    private String title;

    private String contents;
    
    private String plainTags;
    
    private boolean connected = false;
    
    public QuestionDto() {
        
    }
    
    public QuestionDto(String title, String contents, String plainTags) {
    	this(null, title, contents, plainTags);
    }
    
    public QuestionDto(Long questionId, String title, String contents, String plainTags) {
    	this.questionId = questionId;
        this.title = title;
        this.contents = contents;
        this.plainTags = plainTags;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
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
    
    public void setConnected(boolean connected) {
        this.connected = connected;
    }
    
    public boolean isConnected() {
        return this.connected;
    }

    @Override
    public String toString() {
        return "QuestionDto [questionId=" + questionId + ", title=" + title + ", contents=" + contents + ", plainTags="
                + plainTags + "]";
    }
}
