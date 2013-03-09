package net.slipp.service.qna;

import java.util.Set;

import javax.annotation.Resource;

import net.slipp.domain.notification.Notification;
import net.slipp.domain.qna.Answer;
import net.slipp.domain.qna.QnaSpecifications;
import net.slipp.domain.qna.Question;
import net.slipp.domain.qna.QuestionDto;
import net.slipp.domain.tag.Tag;
import net.slipp.domain.user.SocialUser;
import net.slipp.repository.notification.NotificationRepository;
import net.slipp.repository.qna.AnswerRepository;
import net.slipp.repository.qna.QuestionRepository;
import net.slipp.service.rank.ScoreLikeService;
import net.slipp.service.tag.TagService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service("qnaService")
@Transactional
public class QnaService {
    private static Logger log = LoggerFactory.getLogger(QnaService.class);
    
    @Resource(name = "questionRepository")
    private QuestionRepository questionRepository;

    @Resource(name = "answerRepository")
    private AnswerRepository answerRepository;

    @Resource(name = "tagService")
    private TagService tagService;

    @Resource(name = "notificationService")
    private NotificationService notificationService;
    
    @Resource(name = "scoreLikeService")
    private ScoreLikeService scoreLikeService;
    
    @Resource(name = "facebookService")
    private FacebookService facebookService;

    public Question createQuestion(SocialUser loginUser, QuestionDto questionDto) {
        Assert.notNull(loginUser, "loginUser should be not null!");
        Assert.notNull(questionDto, "question should be not null!");

        Set<Tag> tags = tagService.processTags(questionDto.getPlainTags());

        Question newQuestion = new Question(loginUser, questionDto.getTitle(), questionDto.getContents(), tags);
        Question savedQuestion = questionRepository.saveAndFlush(newQuestion);

        if (questionDto.isConnected()) {
            log.info("firing sendMessageToFacebook!");
            facebookService.sendToQuestionMessage(loginUser, savedQuestion.getQuestionId());
        }
        return savedQuestion;
    }

    public Question updateQuestion(SocialUser loginUser, QuestionDto questionDto) {
        Assert.notNull(loginUser, "loginUser should be not null!");
        Assert.notNull(questionDto, "question should be not null!");

        Question question = questionRepository.findOne(questionDto.getQuestionId());

        Set<Tag> tags = tagService.processTags(questionDto.getPlainTags());
        question.update(loginUser, questionDto.getTitle(), questionDto.getContents(), tags);
        return question;
    }

    public void deleteQuestion(SocialUser loginUser, Long questionId) {
        Assert.notNull(loginUser, "loginUser should be not null!");
        Assert.notNull(questionId, "questionId should be not null!");

        Question question = questionRepository.findOne(questionId);
        question.delete(loginUser);
    }

    public Question showQuestion(Long id) {
    	questionRepository.updateShowCount(id);
        return questionRepository.findOne(id);
    }

    public Page<Question> findsByTag(String name, Pageable pageable) {
        return questionRepository.findsByTag(name, pageable);
    }

    public Page<Question> findsQuestion(Pageable pageable) {
        return questionRepository.findAll(QnaSpecifications.equalsIsDelete(false), pageable);
    }

    public Question findByQuestionId(Long id) {
        return questionRepository.findOne(id);
    }

    public Answer findAnswerById(Long answerId) {
        return answerRepository.findOne(answerId);
    }

    public void createAnswer(SocialUser loginUser, Long questionId, Answer answer) {
        Question question = questionRepository.findOne(questionId);
        answer.writedBy(loginUser);
        answer.answerTo(question);
        Answer savedAnswer = answerRepository.saveAndFlush(answer);
        notificationService.notifyToSlipp(question, answer);
        notificationService.notifyToFacebook(loginUser, question, question.findNotificationUser(loginUser));
        if (answer.isConnected()) {
            log.info("firing sendAnswerMessageToFacebook!");
        	facebookService.sendToAnswerMessage(loginUser, savedAnswer.getAnswerId());
        }
    }

    public void updateAnswer(SocialUser loginUser, Answer answerDto) {
        Answer answer = answerRepository.findOne(answerDto.getAnswerId());
        if (!answer.isWritedBy(loginUser)) {
            throw new AccessDeniedException(loginUser + " is not owner!");
        }
        answer.updateAnswer(answerDto);
    }

    public void deleteAnswer(SocialUser loginUser, Long questionId, Long answerId) {
        Assert.notNull(loginUser, "loginUser should be not null!");
        Assert.notNull(questionId, "questionId should be not null!");
        Assert.notNull(answerId, "answerId should be not null!");

        Answer answer = answerRepository.findOne(answerId);
        if (!answer.isWritedBy(loginUser)) {
            throw new AccessDeniedException(loginUser + " is not owner!");
        }
        answerRepository.delete(answer);
        Question question = questionRepository.findOne(questionId);
        question.deAnswered();
    }

    public void likeAnswer(SocialUser loginUser, Long answerId) {
        if (!scoreLikeService.alreadyLikedAnswer(answerId, loginUser.getId())) {
            scoreLikeService.saveLikeAnswer(answerId, loginUser.getId());
            Answer answer = answerRepository.findOne(answerId);
            answer.upRank();
            answerRepository.save(answer);
        }
    }
    
}
