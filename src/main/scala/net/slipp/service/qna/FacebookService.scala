package net.slipp.service.qna

import java.util._
import javax.annotation.Resource

import com.google.common.collect.Lists
import com.restfb._
import com.restfb.exception.FacebookGraphException
import com.restfb.types._
import net.slipp.domain.fb.FacebookComment
import net.slipp.domain.qna.{Answer, Question, SnsConnection}
import net.slipp.domain.tag.Tag
import net.slipp.domain.user.SocialUser
import net.slipp.repository.qna.{AnswerRepository, QuestionRepository}
import net.slipp.repository.tag.TagRepository
import net.slipp.service.user.SocialUserService
import net.slipp.support.web.tags.SlippFunctions
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.core.env.Environment
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.Assert

import scala.collection.JavaConversions._
import scala.util.{Failure, Success, Try}

object FacebookService {
  private val DEFAULT_FACEBOOK_MESSAGE_LENGTH: Int = 250
}

@Service
@Transactional class FacebookService {
  private val log: Logger = LoggerFactory.getLogger(classOf[FacebookService])

  @Autowired private var env: Environment = null
  @Resource(name = "socialUserService") private var socialUserService: SocialUserService = null
  @Resource(name = "questionRepository") private var questionRepository: QuestionRepository = null
  @Resource(name = "answerRepository") private var answerRepository: AnswerRepository = null
  @Resource(name = "tagRepository") private var tagRepository: TagRepository = null

  @Async def sendToQuestionMessage(loginUser: SocialUser, questionId: Long) {
    log.info("questionId : {}", questionId)
    val question: Question = questionRepository.findOne(questionId)
    Assert.notNull(question, "Question should be not null!")
    val message: String = createFacebookMessage(question.getContents)
    val postId: String = sendMessageToFacebook(loginUser, createLink(question.getQuestionId), "me", message)
    if (postId != null) {
      question.connected(postId)
    }
  }

  @annotation.tailrec
  private def retry[T](n: Int)(fn: => T): T = {
    Try { fn } match {
      case Success(x) => x
      case _ if n > 1 => retry(n - 1)(fn)
      case Failure(e) => throw e
    }
  }

  private def sendMessageToFacebook(loginUser: SocialUser, link: String, receiverId: String, message: String) = {
    retry(3) {
      val facebookClient: FacebookClient = createFacebookClient(loginUser)
      val response: FacebookType = facebookClient.publish(receiverId + "/feed", classOf[FacebookType], Parameter.`with`("link", link), Parameter.`with`("message", message))
      response.getId
    }
  }

  private def createFacebookClient(socialUser: SocialUser): FacebookClient = {
    if (socialUser.isFacebookUser) {
      log.debug("access token : {}", socialUser.getAccessToken);
      return new DefaultFacebookClient(socialUser.getAccessToken, Version.VERSION_2_9)
    }
    val adminUser: SocialUser = socialUserService.findAdminUser
    return new DefaultFacebookClient(adminUser.getAccessToken, Version.VERSION_2_9)
  }

  @Async def sendToGroupQuestionMessage(loginUser: SocialUser, questionId: Long) {
    log.info("questionId : {}", questionId)
    val question: Question = questionRepository.findOne(questionId)
    Assert.notNull(question, "Question should be not null!")
    val connectedGroupTags: Set[Tag] = question.getConnectedGroupTag
    if (connectedGroupTags.isEmpty) {
      return
    }
    val message: String = createFacebookMessage(question.getContents)
    val link: String = createLink(question.getQuestionId)
    for (connectedGroupTag <- connectedGroupTags) {
      val postId: String = sendMessageToFacebook(loginUser, link, connectedGroupTag.getTagInfo.getGroupId, message)
      if (postId != null) {
        question.connected(postId, connectedGroupTag.getGroupId)
      }
    }
  }

  @Async def sendToAnswerMessage(loginUser: SocialUser, answerId: Long) {
    log.info("answerId : {}", answerId)
    val answer: Answer = answerRepository.findOne(answerId)
    Assert.notNull(answer, "Answer should be not null!")
    val question: Question = answer.getQuestion
    val message: String = createFacebookMessage(answer.getContents)
    val postId: String = sendMessageToFacebook(loginUser, createLink(question.getQuestionId, answerId), "me", message)
    if (postId != null) {
      answer.connected(postId)
    }
  }

  def findFacebookComments(questionId: Long): List[FacebookComment] = {
    val question: Question = questionRepository.findOne(questionId)
    if (!question.isSnsConnected) {
      return new ArrayList[FacebookComment]
    }
    val socialUser: SocialUser = question.getWriter
    val facebookClient: FacebookClient = createFacebookClient(socialUser)
    val snsConnections: Collection[SnsConnection] = question.getSnsConnection
    val fbComments: List[FacebookComment] = Lists.newArrayList()
    for (snsConnection <- snsConnections) {
      log.debug("postId : {}", snsConnection.getPostId)

      val comments: List[Comment] = findCommentsByPost(facebookClient, snsConnection.getPostId)
      var fbCommentsPerConnection: List[FacebookComment] = null
      if (snsConnection.isGroupConnected) {
        val tag: Tag = tagRepository.findByGroupId(snsConnection.getGroupId)
        fbCommentsPerConnection = comments.map(comment => FacebookComment.create(tag, comment));
      }
      else {
        fbCommentsPerConnection = comments.map(comment => FacebookComment.create(null, comment));
      }
      fbComments.addAll(fbCommentsPerConnection)
      log.debug("count comments : {}, from post : {}", fbCommentsPerConnection.size, snsConnection.getPostId)
      snsConnection.updateAnswerCount(fbCommentsPerConnection.size)
    }
    Collections.sort(fbComments)
    return fbComments
  }

  @Cacheable(value = Array("fbgroups"), key = "#loginUser.id") def findFacebookGroups(loginUser: SocialUser): List[Group] = {
    var groupLimit: Int = 10
    val facebookClient: FacebookClient = createFacebookClient(loginUser)
    val myGroups: Connection[Group] = facebookClient.fetchConnection("/me/groups", classOf[Group], Parameter.`with`("limit", groupLimit))
    val allGroups: List[Group] = Lists.newArrayList()
    for (groups <- myGroups) {
      allGroups.addAll(groups)
    }
    if (allGroups.size < groupLimit) {
      groupLimit = allGroups.size
    }
    return allGroups.subList(0, groupLimit)
  }

  private def findCommentsByPost(facebookClient: FacebookClient, postId: String): List[Comment] = {
    try {
      val fbComments: List[Comment] = Lists.newArrayList()
      val commentConnection = facebookClient.fetchConnection(postId + "/comments", classOf[Comment]);
      if (commentConnection.isEmpty) {
        return fbComments
      }

      import scala.collection.JavaConversions._
      for (commentPage <- commentConnection) {
        for (comment <- commentPage) {
          fbComments.add(comment);
        }
      }
      return fbComments;
    }
    catch {
      case e: FacebookGraphException => {
        log.error(s"${postId} postId, errorMessage : ${e.getMessage}")
        return null
      }
    }
  }

  def findPost(socialUser: SocialUser, postId: String): Post = {
    try {
      val facebookClient: FacebookClient = createFacebookClient(socialUser)
      return facebookClient.fetchObject(postId, classOf[Post])
    }
    catch {
      case e: FacebookGraphException => {
        log.error(s"${postId} postId, errorMessage : ${e.getMessage}")
        return null
      }
    }
  }


  private def create(tag: Tag, comment: Comment): FacebookComment = {
    val user: CategorizedFacebookType = comment.getFrom
    if (tag == null) {
      return new FacebookComment(comment.getId, user.getId, user.getName, comment.getCreatedTime, comment.getMessage, null, null)
    }
    return new FacebookComment(comment.getId, user.getId, user.getName, comment.getCreatedTime, comment.getMessage, tag.getGroupId, tag.getName)
  }

  private[qna] def createLink(questionId: Long): String = {
    s"${createApplicationUrl}/questions/${questionId}"
  }

  private[qna] def createLink(questionId: Long, answerId: Long): String = {
    s"${createApplicationUrl}/questions/${questionId}#answer-${answerId}"
  }

  protected def createApplicationUrl = {
    env.getProperty("facebook.application.url")
  }

  private def createFacebookMessage(contents: String): String = {
    val wikiContents: String = SlippFunctions.wiki(contents)
    return SlippFunctions.stripTagsAndCut(wikiContents, FacebookService.DEFAULT_FACEBOOK_MESSAGE_LENGTH, "...")
  }
}
