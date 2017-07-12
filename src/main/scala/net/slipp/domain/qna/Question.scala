package net.slipp.domain.qna

import java.lang.Long
import java.util._
import javax.persistence._

import com.google.common.collect.{Iterables, Lists, Sets}
import net.slipp.domain.ProviderType
import net.slipp.domain.tag.{Tag, Tags}
import net.slipp.domain.user.SocialUser
import net.slipp.service.qna.QnaService
import net.slipp.support.jpa.{DomainModel, CreatedDateEntityListener, HasCreatedDate}
import net.slipp.support.wiki.SlippWikiUtils
import org.apache.commons.collections4.CollectionUtils
import org.apache.commons.lang3.StringUtils
import org.hibernate.annotations.{Cache, CacheConcurrencyStrategy, Where}
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.security.access.AccessDeniedException

import scala.collection.JavaConversions._

object Question {
  private val log: Logger = LoggerFactory.getLogger(classOf[QnaService])

  private val DEFAULT_BEST_ANSWER: Integer = 2

  private[qna] def detaggedTags(originalTags: Set[Tag]) {
    originalTags.foreach(t => t.deTagged())
  }

  private[qna] def taggedTags(newTags: Set[Tag]) {
    newTags.foreach(t => t.tagged())
  }
}

@Entity
@EntityListeners(Array(classOf[CreatedDateEntityListener]))
@Cache(region = "question", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
class Question(id: Long, loginUser: SocialUser, t: String, c: String, nTags: Set[Tag]) extends DomainModel with HasCreatedDate {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private var questionId: Long = id

  @ManyToOne
  @JoinColumn(foreignKey = new ForeignKey(name = "fk_question_writer"))
  private var writer: SocialUser = loginUser

  @ManyToOne
  @JoinColumn(foreignKey = new ForeignKey(name = "fk_question_latest_participant"))
  private var latestParticipant: SocialUser = loginUser

  @Column(name = "title", length = 100, nullable = false)
  private var title: String = t

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "question_content_holder",
    joinColumns = Array(new JoinColumn(name = "question_id", unique = true,
      foreignKey = new ForeignKey(name = "fk_question_content_holder_question_id"))))
  @Lob
  @Column(name = "contents", nullable = false)
  private var contentsHolder: Collection[String] = Lists.newArrayList(c)

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_date", nullable = false, updatable = false)
  private var createdDate: Date = null

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "updated_date", nullable = false)
  private var updatedDate: Date = new Date()

  @Column(name = "answer_count", nullable = false)
  private var answerCount: Int = 0

  @Column(name = "show_count", nullable = false)
  private var showCount: Int = 0

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "question_tag",
    joinColumns = Array(new JoinColumn(name = "question_id",
      foreignKey = new ForeignKey(name = "fk_question_tag_question_id"))),
    inverseJoinColumns = Array(new JoinColumn(name = "tag_id",
      foreignKey = new ForeignKey(name = "fk_question_tag_tag_id"))))
  private var tags: Set[Tag] = Sets.newHashSet[Tag]

  @Column(name = "denormalized_tags", length = 100)
  private var denormalizedTags: String = _

  @Column(name = "sum_like", nullable = true, columnDefinition = "integer DEFAULT 0")
  private var sumLike: Integer = 0

  @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
  @Where(clause = "deleted = 0")
  @OrderBy("answerId ASC")
  private var answers: List[Answer] = _

  @Column(name = "deleted", nullable = false)
  private var deleted: Boolean = false

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "question_sns_connections",
    joinColumns = Array(new JoinColumn(name = "question_id",
      foreignKey = new ForeignKey(name = "fk_question_sns_connection_question_id"))))
  private var snsConnetions: Set[SnsConnection] = Sets.newHashSet[SnsConnection]

  Option(nTags) match {
    case Some(tags) => newTags(tags)
    case None =>
  }

  def this(loginUser: SocialUser, title: String, contents: String, pooledTags: Set[Tag]) =
    this(0L, loginUser, title, contents, pooledTags)

  def this() = this(null, null, null, Sets.newHashSet[Tag]())

  def getAnswers: List[Answer] = {
    return answers
  }

  def setAnswers(answers: List[Answer]) {
    this.answers = answers
  }

  def getAnswerCount: Int = {
    return answerCount
  }

  def getSnsAnswerCount: Int = {
    var snsAnswerCount: Int = 0
    import scala.collection.JavaConversions._
    for (each <- snsConnetions) {
      snsAnswerCount += each.getSnsAnswerCount
    }
    return snsAnswerCount
  }

  def getTotalAnswerCount: Int = {
    return answerCount + getSnsAnswerCount
  }

  def getTags: Set[Tag] = {
    return tags
  }

  def getDenormalizedTags: String = {
    return this.denormalizedTags
  }

  private def isEmptyContentsHolder: Boolean = {
    return contentsHolder == null || contentsHolder.isEmpty
  }

  def getContents: String = {
    if (isEmptyContentsHolder) {
      return ""
    }
    return Iterables.getFirst(contentsHolder, "")
  }

  def getQuestionId: Long = {
    return questionId
  }

  def isWritedBy(socialUser: SocialUser): Boolean = {
    return writer.isSameUser(socialUser)
  }

  def getWriter: SocialUser = {
    return writer
  }

  def getLatestParticipant: SocialUser = {
    return this.latestParticipant
  }

  def getTitle: String = {
    return title
  }

  def getSummaryTitle: String = {
    return StringUtils.left(title, 8) + "..."
  }

  def getShowCount: Int = {
    return showCount
  }

  def getSumLike: Integer = {
    return sumLike
  }

  def getPlainTags: String = {
    var displayTags: String = ""
    for (tag <- getTags) {
      displayTags += tag.getName + " "
    }
    return displayTags
  }

  def getCreatedDate: Date = {
    return createdDate
  }

  def setCreatedDate(createdDate: Date) {
    this.createdDate = createdDate
  }

  def getUpdatedDate: Date = {
    return updatedDate
  }

  def isDeleted: Boolean = {
    return deleted
  }

  def delete(loginUser: SocialUser) {
    if (!isWritedBy(loginUser)) {
      throw new AccessDeniedException(loginUser.getDisplayName + " is not owner!")
    }
    this.deleted = true
    Question.detaggedTags(this.tags)
  }

  def newAnswered(answer: Answer) {
    this.answerCount += 1
    this.latestParticipant = answer.getWriter
    this.updatedDate = new Date
  }

  def moveAnswered(answer: Answer) {
    this.answerCount += 1
    this.latestParticipant = answer.getWriter
    this.updatedDate = answer.getCreatedDate
  }

  def deAnswered(answer: Answer) {
    answer.delete()
    syncAnswer(answer)
  }

  private def syncAnswer(answer: Answer) {
    this.answerCount -= 1
    if (this.answerCount == 0) {
      this.latestParticipant = getWriter
      this.updatedDate = getCreatedDate
      return
    }
    val activeAnswers = getAnswers.filter(a => !a.isDeleted)
    val lastAnswer: Answer = Iterables.getLast(activeAnswers)
    Question.log.debug("Latest Answer : {}", lastAnswer)
    this.latestParticipant = lastAnswer.getWriter
    this.updatedDate = lastAnswer.getCreatedDate
  }

  def tagsToDenormalizedTags {
    this.denormalizedTags = TagHelper.denormalizedTags(getTags)
  }

  def hasTag(tag: Tag): Boolean = {
    return tags.contains(tag)
  }

  def newTags(nTags: Set[Tag]) {
    Question.detaggedTags(tags)
    Question.taggedTags(nTags)
    this.tags = nTags
    tagsToDenormalizedTags
  }

  def differenceTags(newTags: Set[Tag]): DifferenceTags = {
    val oldTags: Set[Tag] = Collections.unmodifiableSet(this.tags)
    return new DifferenceTags(oldTags, newTags)
  }

  def taggedTag(newTag: Tag) {
    newTag.tagged
    tags.add(newTag)
    tagsToDenormalizedTags
  }

  def detaggedTag(tag: Tag) {
    tag.deTagged
    tags.remove(tag)
    tagsToDenormalizedTags
  }

  def update(loginUser: SocialUser, title: String, contents: String, nTags: java.util.Set[Tag]) {
    if (!isWritedBy(loginUser)) {
      throw new AccessDeniedException(loginUser.getDisplayName + " is not owner!")
    }
    this.title = title
    this.contentsHolder = Lists.newArrayList(contents)
    newTags(nTags)
    this.updatedDate = new Date
    this.latestParticipant = getWriter
  }

  def updateContentsByAdmin(contents: String) {
    this.contentsHolder = Lists.newArrayList(contents)
  }

  def findNotificationUser(loginUser: SocialUser) = {
    val participants = answers.map(a => a.getWriter) :+ this.writer
    participants.toSet.filter(u => u.isFacebookUser && !u.isSameUser(loginUser))
  }

  def connected(postId: String): SnsConnection = {
    return connected(postId, null)
  }

  def connected(postId: String, groupId: String): SnsConnection = {
    val snsConnection: SnsConnection = new SnsConnection(ProviderType.valueOf(writer.getProviderId), postId, groupId)
    snsConnetions.add(snsConnection)
    return snsConnection
  }

  def getSnsConnection: Collection[SnsConnection] = {
    return snsConnetions
  }

  def isSnsConnected: Boolean = {
    return !snsConnetions.isEmpty
  }

  def migrateFacebookPostId: Boolean = {
    if (!writer.isFacebookUser) {
      return false
    }

    if (!isSnsConnected) {
      return false
    }

    val providerUserId = writer.getProviderUserId
    var connetions = getSnsConnection
    connetions.foreach(connection => {
      connection.migrationPostId(providerUserId)
    })

    return true;
  }

  /**
    * 베스트 댓글 하나를 반환한다.
    *
    * @return
    */
  def getBestAnswer: Answer = {
    if (CollectionUtils.isEmpty(getAnswers)) {
      return null
    }
    val answer: Answer = getTopLikeAnswer
    if (!answer.likedMoreThan(Question.DEFAULT_BEST_ANSWER)) {
      return null
    }
    return answer
  }

  def upRank {
    this.sumLike += 1
  }

  private def getTopLikeAnswer: Answer = {
    val sortAnswers = Lists.newArrayList[Answer]
    sortAnswers.addAll(getAnswers)
    Collections.sort(sortAnswers)
    return sortAnswers.get(0)
  }

  def getConnectedGroupTag: Set[Tag] = {
    return new Tags(tags).getConnectedGroupTags
  }

  def convertWiki() {
    val contents: String = SlippWikiUtils.convertWiki(getContents)
    this.contentsHolder = Lists.newArrayList(contents)
  }

  def moveAnswers(question: Question, moveAnswers: Array[Long]) {
    Question.log.debug("count of Answers : {}", moveAnswers.size)
    val filteredAnswers = getAnswers.filter(a => moveAnswers.contains(a.getAnswerId))
    filteredAnswers.foreach(a => {
      a.toQuestion(question);
      getAnswers.remove(a);
      syncAnswer(a);
    })
  }
}

