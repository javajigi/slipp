package net.slipp.domain.qna;

import java.io.Serializable;

public class TemporaryAnswer implements Serializable {
    private static final long serialVersionUID = -4198138126883127272L;
    
    public static final String TEMPORARY_ANSWER_KEY = "temporaryAnswer";
    public static final TemporaryAnswer EMPTY_ANSWER = new EmptyTemporaryAnswer();
    
    private Long questionId;
    private String temporaryAnswer;
    
    public TemporaryAnswer() {
    }

    public TemporaryAnswer(Long questionId, String temporaryAnswer) {
        this.questionId = questionId;
        this.temporaryAnswer = temporaryAnswer;
    }
    
    public Long getQuestionId() {
        return questionId;
    }
    
    public String getTemporaryAnswer() {
        return temporaryAnswer;
    }
    
    public Answer createAnswer() {
        return new Answer(this.temporaryAnswer);
    }
    
    public String generateUrl() {
        return String.format("/questions/%d", this.questionId);
    }
    
    static class EmptyTemporaryAnswer extends TemporaryAnswer {
        private static final long serialVersionUID = 1380330064986704887L;
        
        public Answer createAnswer() {
            return new Answer();
        }
        
        public String generateUrl() {
            return "/";
        }
    }

    @Override
    public String toString() {
        return "TemporaryAnswer [questionId=" + questionId + ", temporaryAnswer=" + temporaryAnswer + "]";
    }
}
