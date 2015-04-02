package net.slipp.service.qna;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import net.slipp.domain.qna.Answer;
import net.slipp.domain.qna.DifferenceTags;
import net.slipp.domain.qna.QnaSpecifications;
import net.slipp.domain.qna.Question;
import net.slipp.domain.qna.QuestionDto;
import net.slipp.domain.tag.Tag;
import net.slipp.domain.tag.TaggedType;
import net.slipp.domain.user.SocialUser;
import net.slipp.repository.qna.AnswerRepository;
import net.slipp.repository.qna.QuestionRepository;
import net.slipp.service.rank.ScoreLikeService;
import net.slipp.service.tag.TagService;
import net.slipp.service.user.SocialUserService;

import org.apache.commons.lang3.StringUtils;
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

		final Set<Tag> tags = tagService.processTags(questionDto.getPlainTags());
		final Set<Tag> groupTags = tagService.processGroupTags(questionDto.getFacebookGroups());
		log.debug("group tag size : {}", groupTags.size());
		tags.addAll(groupTags);

		Question newQuestion = new Question(loginUser, questionDto.getTitle(), questionDto.getContents(), tags);
		final Question savedQuestion = questionRepository.save(newQuestion);
		tagService.saveTaggedHistories(savedQuestion, tags);

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

		Question savedQuestion = questionRepository.findOne(questionDto.getQuestionId());

		final Set<Tag> tags = tagService.processTags(questionDto.getPlainTags());
		final DifferenceTags differenceTags = savedQuestion.differenceTags(tags);
		final Set<Tag> newTags = differenceTags.taggedNewTags();
		savedQuestion.update(loginUser, questionDto.getTitle(), questionDto.getContents(), tags);
		tagService.saveTaggedHistories(savedQuestion, newTags);
		return savedQuestion;
	}
	
	public Question updateQuestionByAdmin(SocialUser loginUser, QuestionDto questionDto) {
		Assert.notNull(loginUser, "loginUser should be not null!");
		Assert.notNull(questionDto, "question should be not null!");

		Question savedQuestion = questionRepository.findOne(questionDto.getQuestionId());
		savedQuestion.updateContentsByAdmin(questionDto.getContents());
		return savedQuestion;
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
	
	public Page<Question> findsAllQuestion(String searchTerm, Pageable pageable) {
		if (StringUtils.isBlank(searchTerm)) {
			return questionRepository.findAll(pageable);
		}
		return questionRepository.findsBySearch(searchTerm, pageable);
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
	
	private void deleteAnswer(SocialUser loginUser, Question question, Answer answer) {
		deleteAnswer(loginUser, question.getQuestionId(), answer.getAnswerId());
	}

	public Answer likeAnswer(SocialUser loginUser, Long answerId) {
	    Answer answer = answerRepository.findOne(answerId);
		if (!scoreLikeService.alreadyLikedAnswer(answerId, loginUser.getId())) {
			scoreLikeService.saveLikeAnswer(answerId, loginUser.getId());
			answer.upRank();
		}
		return answer;
	}

    public Answer dislikeAnswer(SocialUser loginUser, Long answerId) {
        Answer answer = answerRepository.findOne(answerId);
        if (!scoreLikeService.alreadyDisLikedAnswer(answerId, loginUser.getId())) {
            scoreLikeService.saveDisLikeAnswer(answerId, loginUser.getId());
            answer.downRank();
        }
        return answer;
    }

    public Question likeQuestion(SocialUser loginUser, Long questionId) {
        Question question = questionRepository.findOne(questionId);
        if (!scoreLikeService.alreadyLikedQuestion(questionId, loginUser.getId())) {
            scoreLikeService.saveLikeQuestion(questionId, loginUser.getId());
            question.upRank();
        }
        return question;
    }

	public void tagged(SocialUser loginUser, Long id, String taggedName) {
		Question question = questionRepository.findOne(id);
		Tag newTag = tagService.findTagByName(taggedName);
		if (newTag == null) {
			newTag = tagService.newTag(taggedName);
		}
		if (question.hasTag(newTag)) {
			return;
		}
		question.taggedTag(newTag);
		tagService.saveTaggedHistory(loginUser, question, newTag, TaggedType.TAGGED);
	}

	public void detagged(SocialUser loginUser, Long id, String taggedName) {
		Question question = questionRepository.findOne(id);
		Tag tag = tagService.findTagByName(taggedName);
		if (tag == null) {
			throw new NullPointerException(String.format("%s tag does not exist.", taggedName));
		}
		if (!question.hasTag(tag)) {
			return;
		}
		question.detaggedTag(tag);
		tagService.saveTaggedHistory(loginUser, question, tag, TaggedType.DETAGGED);
	}

	public void deleteToBlock(SocialUser user) {
		List<Answer> answers = answerRepository.findByWriter(user);
		for (Answer answer : answers) {
			deleteAnswer(user, answer.getQuestion(), answer);
		}
		
		List<Question> questions = questionRepository.findByWriter(user);
		for (Question question : questions) {
			question.delete(user);
		}
	}
}
