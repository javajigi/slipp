package net.slipp.service.qna

import java.util.Set
import javax.annotation.Resource

import com.restfb.types.Post
import net.slipp.domain.qna.{Answer, DifferenceTags, Question, _}
import net.slipp.domain.tag.{Tag, TaggedType}
import net.slipp.domain.user.SocialUser
import net.slipp.repository.qna.{AnswerRepository, QuestionRepository}
import net.slipp.service.rank.ScoreLikeService
import net.slipp.service.tag.TagService
import net.slipp.service.user.SocialUserService
import org.apache.commons.lang3.StringUtils
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.data.domain.{Page, Pageable}
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.{TransactionSynchronizationAdapter, TransactionSynchronizationManager}
import org.springframework.util.Assert

import scala.collection.JavaConversions._

@Service("qnaService")
@Transactional class QnaService {
  val log: Logger = LoggerFactory.getLogger(classOf[QnaService])

  @Resource(name = "questionRepository") private var questionRepository: QuestionRepository = null
  @Resource(name = "answerRepository") private var answerRepository: AnswerRepository = null
  @Resource(name = "tagService") private var tagService: TagService = null
  @Resource(name = "notificationService") private var notificationService: NotificationService = null
  @Resource(name = "scoreLikeService") private var scoreLikeService: ScoreLikeService = null
  @Resource(name = "facebookService") private var facebookService: FacebookService = null
  @Resource(name = "socialUserService") private var socialUserService: SocialUserService = null

  def createQuestion(loginUser: SocialUser, questionDto: QuestionDto): Question = {
    Assert.notNull(loginUser, "loginUser should be not null!")
    Assert.notNull(questionDto, "question should be not null!")
    val tags: Set[Tag] = tagService.processTags(questionDto.getPlainTags)
    val groupTags: Set[Tag] = tagService.processGroupTags(questionDto.getFacebookGroups)
    log.debug("group tag size : {}", groupTags.size)
    tags.addAll(groupTags)
    val newQuestion: Question = new Question(loginUser, questionDto.getTitle, questionDto.getContents, tags)
    val savedQuestion: Question = questionRepository.save(newQuestion)
    tagService.saveTaggedHistories(savedQuestion, tags)
    TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
      override def afterCommit {
        if (questionDto.getConnected) {
          facebookService.sendToQuestionMessage(loginUser, savedQuestion.getQuestionId)
        }
        if (!groupTags.isEmpty) {
          facebookService.sendToGroupQuestionMessage(loginUser, savedQuestion.getQuestionId)
        }
      }
    })
    return savedQuestion
  }

  def updateQuestion(loginUser: SocialUser, questionDto: QuestionDto): Question = {
    Assert.notNull(loginUser, "loginUser should be not null!")
    Assert.notNull(questionDto, "question should be not null!")
    val savedQuestion: Question = questionRepository.findOne(questionDto.getQuestionId)
    val tags: Set[Tag] = tagService.processTags(questionDto.getPlainTags)
    val differenceTags: DifferenceTags = savedQuestion.differenceTags(tags)
    val newTags: Set[Tag] = differenceTags.taggedNewTags
    savedQuestion.update(loginUser, questionDto.getTitle, questionDto.getContents, tags)
    tagService.saveTaggedHistories(savedQuestion, newTags)
    return savedQuestion
  }

  def updateQuestionByAdmin(loginUser: SocialUser, questionDto: QuestionDto): Question = {
    Assert.notNull(loginUser, "loginUser should be not null!")
    Assert.notNull(questionDto, "question should be not null!")
    val savedQuestion: Question = questionRepository.findOne(questionDto.getQuestionId)
    savedQuestion.updateContentsByAdmin(questionDto.getContents)
    return savedQuestion
  }

  def deleteQuestion(loginUser: SocialUser, questionId: Long) {
    Assert.notNull(loginUser, "loginUser should be not null!")
    Assert.notNull(questionId, "questionId should be not null!")
    val question: Question = questionRepository.findOne(questionId)
    question.delete(loginUser)
  }

  def showQuestion(id: Long): Question = {
    questionRepository.updateShowCount(id)
    return questionRepository.findOne(id)
  }

  def findsByTag(name: String, pageable: Pageable): Page[Question] = {
    return questionRepository.findsByTag(name, pageable)
  }

  def findsQuestion(pageable: Pageable): Page[Question] = {
    questionRepository.findByDeleted(false, pageable)
  }

  def findsAllQuestion(searchTerm: String, pageable: Pageable): Page[Question] = {
    if (StringUtils.isBlank(searchTerm)) {
      return questionRepository.findAll(pageable)
    }
    return questionRepository.findsBySearch(searchTerm, pageable)
  }

  def findsQuestionByWriter(writerId: Option[Long], pageable: Pageable): Page[Question] = {
    writerId match {
      case Some(w) => {
        val writer: SocialUser = socialUserService.findById(w)
        questionRepository.findByWriterAndDeleted(writer, false, pageable)
      }
      case None => questionRepository.findByDeleted(false, pageable)
    }
  }

  def findByQuestionId(id: Long): Question = {
    return questionRepository.findOne(id)
  }

  def findAnswerById(answerId: Long): Answer = {
    return answerRepository.findOne(answerId)
  }

  def findsAnswerByWriter(writerId: Long, pageable: Pageable): Page[Question] = {
    val writer: SocialUser = socialUserService.findById(writerId)
    return questionRepository.findsAnswerByWriter(writer, pageable)
  }

  def createAnswer(loginUser: SocialUser, questionId: Long, answer: Answer) {
    val question: Question = questionRepository.findOne(questionId)
    answer.writedBy(loginUser)
    answer.answerTo(question)
    val savedAnswer: Answer = answerRepository.save(answer)
    TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
      override def afterCommit {
        notificationService.notifyToSlipp(loginUser, savedAnswer.getAnswerId)
        notificationService.notifyToFacebook(loginUser, savedAnswer.getAnswerId)
        if (answer.isConnected) {
          facebookService.sendToAnswerMessage(loginUser, savedAnswer.getAnswerId)
        }
      }
    })
  }

  def updateAnswer(loginUser: SocialUser, answerDto: Answer) {
    val answer: Answer = answerRepository.findOne(answerDto.getAnswerId)
    if (!answer.isWritedBy(loginUser)) {
      throw new AccessDeniedException(loginUser + " is not owner!")
    }
    answer.updateAnswer(answerDto)
  }

  def deleteAnswer(loginUser: SocialUser, questionId: Long, answerId: Long) {
    Assert.notNull(loginUser, "loginUser should be not null!")
    Assert.notNull(questionId, "questionId should be not null!")
    Assert.notNull(answerId, "answerId should be not null!")

    val answer: Answer = answerRepository.findOne(answerId)
    if (!answer.isWritedBy(loginUser)) {
      throw new AccessDeniedException(loginUser + " is not owner!")
    }
    val question: Question = questionRepository.findOne(questionId)
    question.deAnswered(answer)
  }

  private def deleteAnswer(loginUser: SocialUser, question: Question, answer: Answer) {
    deleteAnswer(loginUser, question.getQuestionId, answer.getAnswerId)
  }

  def likeAnswer(loginUser: SocialUser, answerId: Long): Answer = {
    val answer: Answer = answerRepository.findOne(answerId)
    if (!scoreLikeService.alreadyLikedAnswer(answerId, loginUser.getId)) {
      scoreLikeService.saveLikeAnswer(answerId, loginUser.getId)
      answer.upRank
    }
    return answer
  }

  def dislikeAnswer(loginUser: SocialUser, answerId: Long): Answer = {
    val answer: Answer = answerRepository.findOne(answerId)
    if (!scoreLikeService.alreadyDisLikedAnswer(answerId, loginUser.getId)) {
      scoreLikeService.saveDisLikeAnswer(answerId, loginUser.getId)
      answer.downRank
    }
    return answer
  }

  def likeQuestion(loginUser: SocialUser, questionId: Long): Question = {
    val question: Question = questionRepository.findOne(questionId)
    if (!scoreLikeService.alreadyLikedQuestion(questionId, loginUser.getId)) {
      scoreLikeService.saveLikeQuestion(questionId, loginUser.getId)
      question.upRank
    }
    return question
  }

  def tagged(loginUser: SocialUser, id: Long, taggedName: String) {
    val question: Question = questionRepository.findOne(id)
    var newTag: Tag = tagService.findTagByName(taggedName)
    if (newTag == null) {
      newTag = tagService.newTag(taggedName)
    }
    if (question.hasTag(newTag)) {
      return
    }
    question.taggedTag(newTag)
    tagService.saveTaggedHistory(loginUser, question, newTag, TaggedType.TAGGED)
  }

  def detagged(loginUser: SocialUser, id: Long, taggedName: String) {
    val question: Question = questionRepository.findOne(id)
    val tag: Tag = tagService.findTagByName(taggedName)
    if (tag == null) {
      throw new NullPointerException(String.format("%s tag does not exist.", taggedName))
    }
    if (!question.hasTag(tag)) {
      return
    }
    question.detaggedTag(tag)
    tagService.saveTaggedHistory(loginUser, question, tag, TaggedType.DETAGGED)
  }

  def deleteToBlock(user: SocialUser) {
    val answers = answerRepository.findByWriter(user)
    for (answer <- answers) {
      deleteAnswer(user, answer.getQuestion, answer)
    }
    val questions = questionRepository.findByWriter(user)
    for (question <- questions) {
      question.delete(user)
    }
  }

  def connectFB(id: Long, postId: String) {
    val adminUser: SocialUser = socialUserService.findAdminUser
    val post: Post = facebookService.findPost(adminUser, postId)
    if (post == null) {
      throw new IllegalArgumentException(postId + "는 유효하지 않은 포스트입니다.")
    }
    val question = questionRepository.findOne(id)
    question.connected(postId)
  }

  def moveQuestion(loginUser: SocialUser, id: Long, newQuestion: QuestionDto) = {
    val answer = answerRepository.findOne(newQuestion.getOriginalAnswerId)
    if (!(answer.isWritedBy(loginUser) || loginUser.isAdmined)) {
      throw new AccessDeniedException(loginUser.getDisplayName + " is not owner!")
    }

    val question = createQuestion(answer.getWriter, newQuestion)
    answerRepository.delete(answer)
    val originalQuestion = questionRepository.findOne(id)
    originalQuestion.moveAnswers(question, newQuestion.getMoveAnswers)
    question
  }
}

