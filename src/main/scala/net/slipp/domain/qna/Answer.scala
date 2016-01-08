package net.slipp.domain.qna

import com.google.common.collect.Iterables
import com.google.common.collect.Lists
import net.slipp.domain.ProviderType
import net.slipp.domain.user.SocialUser
import net.slipp.support.jpa.{DomainModel, CreatedAndUpdatedDateEntityListener, HasCreatedAndUpdatedDate}
import net.slipp.support.wiki.SlippWikiUtils
import javax.persistence._
import java.util.Collection
import java.util.Date

@Entity
@EntityListeners(Array(classOf[CreatedAndUpdatedDateEntityListener]))
class Answer(c: String) extends DomainModel with HasCreatedAndUpdatedDate with Comparable[Answer] {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "answer_id")
  private var answerId: Long = _

  @ManyToOne
  @JoinColumn(name = "writer", foreignKey = new ForeignKey(name = "fk_answer_writer"))
  private var writer: SocialUser = _

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "answer_content_holder",
    joinColumns = Array(new JoinColumn(name = "answer_id", unique = true,
      foreignKey = new ForeignKey(name = "fk_answer_content_holder_answer_id"))))
  @Lob
  @Column(name = "contents", nullable = false)
  private var contentsHolder: Collection[String] = null

  @Transient
  private var connected: Boolean = false

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_date", nullable = false, updatable = false)
  private var createdDate: Date = null

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "updated_date", nullable = false)
  private var updatedDate: Date = null

  @Column(name = "sum_like", nullable = true, columnDefinition = "integer DEFAULT 0")
  private var sumLike: Integer = 0

  @Column(name = "sum_dislike", nullable = true, columnDefinition = "integer DEFAULT 0")
  private var sumDislike: Integer = 0

  @Column(name = "deleted", nullable = false)
  private var deleted: Boolean = false

  @ManyToOne
  @JoinColumn(name = "question", foreignKey = new ForeignKey(name = "fk_answer_parent_id"))
  private var question: Question = null

  @Embedded
  private var snsConnection: SnsConnection = new SnsConnection

  setContents(c)

  def this(answerId: Long) = {
    this(null)
    this.answerId = answerId
  }

  def this() = this(null)

  def getQuestion: Question = {
    return this.question
  }

  def setQuestion(question: Question) {
    this.question = question
  }

  def getAnswerId: Long = {
    return answerId
  }

  def setAnswerId(answerId: Long) {
    this.answerId = answerId
  }

  def getWriter: SocialUser = {
    return writer
  }

  def setContents(newContents: String) {
    if (isEmptyContentsHolder) {
      contentsHolder = Lists.newArrayList(newContents)
    }
    else {
      contentsHolder.clear
      contentsHolder.add(newContents)
    }
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

  def getCreatedDate: Date = {
    return createdDate
  }

  def setCreatedDate(createdDate: Date) {
    this.createdDate = createdDate
  }

  def getUpdatedDate: Date = {
    return updatedDate
  }

  def setUpdatedDate(updatedDate: Date) {
    this.updatedDate = updatedDate
  }

  def answerTo(question: Question) {
    this.question = question
    question.newAnswered(this)
  }

  def writedBy(user: SocialUser) {
    this.writer = user
  }

  def isWritedBy(loginUser: SocialUser): Boolean = {
    return writer.isSameUser(loginUser)
  }

  def isFacebookWriter: Boolean = {
    return writer.isFacebookUser
  }

  def setConnected(connected: Boolean) {
    this.connected = connected
  }

  def isConnected: Boolean = {
    return connected
  }

  def updateAnswer(answerDto: Answer) {
    this.contentsHolder = answerDto.contentsHolder
  }

  def getSumLike: Integer = {
    return sumLike
  }

  def getSumDislike: Integer = {
    return sumDislike
  }

  def isDeleted: Boolean = {
    return deleted
  }

  def delete() {
    this.deleted = true
  }

  def upRank() {
    this.sumLike += 1
  }

  def downRank() {
    this.sumDislike += 1
  }

  private[qna] def likedMoreThan(totalLiked: Int): Boolean = {
    if (getSumLike >= totalLiked) {
      return true
    }
    return false
  }

  def toQuestion(question: Question) {
    this.question = question
    question.moveAnswered(this)
  }

  def connected(postId: String): SnsConnection = {
    this.snsConnection = new SnsConnection(ProviderType.valueOf(writer.getProviderId), postId)
    return this.snsConnection
  }

  def convertWiki() {
    val contents: String = SlippWikiUtils.convertWiki(getContents)
    this.contentsHolder = Lists.newArrayList(contents)
  }

  def compareTo(o: Answer): Int = {
    return o.getSumLike.compareTo(getSumLike)
  }
}
