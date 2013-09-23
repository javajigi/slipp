package net.slipp.service.qna;

import java.util.Set;

import javax.annotation.Resource;

import net.slipp.domain.qna.Answer;
import net.slipp.domain.qna.QnaSpecifications;
import net.slipp.domain.qna.Question;
import net.slipp.domain.qna.QuestionDto;
import net.slipp.domain.tag.Tag;
import net.slipp.domain.user.SocialUser;
import net.slipp.repository.qna.AnswerRepository;
import net.slipp.repository.qna.QuestionRepository;
import net.slipp.service.rank.ScoreLikeService;
import net.slipp.service.tag.TagService;
import net.slipp.service.user.SocialUserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
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

	@Resource(name = "socialUserService")
	private SocialUserService socialUserService;

	public Question createQuestion(final SocialUser loginUser, final QuestionDto questionDto) {
		Assert.notNull(loginUser, "loginUser should be not null!");
		Assert.notNull(questionDto, "question should be not null!");

		Set<Tag> tags = tagService.processTags(questionDto.getPlainTags());
		final Set<Tag> groupTags = tagService.processGroupTags(questionDto.getFacebookGroups());
		log.debug("group tag size : {}", groupTags.size());
		tags.addAll(groupTags);

		Question newQuestion = new Question(loginUser, questionDto.getTitle(), questionDto.getContents(), tags);
		final Question savedQuestion = questionRepository.save(newQuestion);

		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
			public void afterCommit() {
			    if (questionDto.isConnected()) {
			        facebookService.sendToQuestionMessage(loginUser, savedQuestion.getQuestionId());
			    }
			    
			    if (!groupTags.isEmpty()) {
			        facebookService.sendToGroupQuestionMessage(loginUser, savedQuestion.getQuestionId());
			    }
			}
		});
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
		return questionRepository.findAll(QnaSpecifications.equalsIsDeleteToQuestion(false), pageable);
	}

	public Page<Question> findsQuestionByWriter(Long writerId, Pageable pageable) {
		if (writerId == null) {
			return questionRepository.findAll(QnaSpecifications.equalsIsDeleteToQuestion(false), pageable);
		}

		SocialUser writer = socialUserService.findById(writerId);
		return questionRepository.findAll(QnaSpecifications.findQuestions(writer, false), pageable);
	}

	public Question findByQuestionId(Long id) {
		return questionRepository.findOne(id);
	}

	public Answer findAnswerById(Long answerId) {
		return answerRepository.findOne(answerId);
	}

	public Page<Answer> findsAnswerByWriter(Long writerId, Pageable pageable) {
		SocialUser writer = socialUserService.findById(writerId);
		return answerRepository.findsAnswerByWriter(writer, pageable);
	}

	public void createAnswer(final SocialUser loginUser, Long questionId, final Answer answer) {
		final Question question = questionRepository.findOne(questionId);
		answer.writedBy(loginUser);
		answer.answerTo(question);
		final Answer savedAnswer = answerRepository.save(answer);

		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
			public void afterCommit() {
				notificationService.notifyToSlipp(loginUser, savedAnswer.getAnswerId());
				notificationService.notifyToFacebook(loginUser, savedAnswer.getAnswerId());
				if (answer.isConnected()) {
					facebookService.sendToAnswerMessage(loginUser, savedAnswer.getAnswerId());
				}
			}
		});
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
		question.deAnswered(answer);
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
