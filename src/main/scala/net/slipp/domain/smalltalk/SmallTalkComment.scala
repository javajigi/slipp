package net.slipp.domain.smalltalk

import java.util.Date
import javax.persistence._
import javax.validation.constraints.NotNull

import net.slipp.domain.user.SocialUser
import net.slipp.support.jpa.{CreatedAndUpdatedDateEntityListener, HasCreatedAndUpdatedDate}
import org.apache.commons.lang3.StringUtils
import org.hibernate.validator.constraints.Length

@Entity
@EntityListeners(Array(classOf[CreatedAndUpdatedDateEntityListener]))
class SmallTalkComment extends HasCreatedAndUpdatedDate {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private var smallTalkCommentId: Long = _

  @NotNull
  @Length(min = 1, max = 200)
  @Column(name = "comments", length = 255, nullable = false)
  private var comments: String = null

  @ManyToOne
  @JoinColumn(foreignKey = new ForeignKey(name = "fk_smalltalkcomment_writer"))
  private var writer: SocialUser = null

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_date", nullable = false, updatable = false)
  private var createdDate: Date = null

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "updated_date", nullable = false)
  private var updatedDate: Date = null

  @ManyToOne
  @JoinColumn(foreignKey = new ForeignKey(name = "fk_smalltalkcomment_parent_id"))
  private var smallTalk: SmallTalk = null

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

  def getSmallTalk: SmallTalk = {
    return smallTalk
  }

  def setSmallTalk(smallTalk: SmallTalk) {
    this.smallTalk = smallTalk
  }

  def commentTo(smallTalk: SmallTalk) {
    setSmallTalk(smallTalk)
  }

  def getWriter: SocialUser = {
    return writer
  }

  def setWriter(writer: SocialUser) {
    this.writer = writer
  }

  def getComments: String = {
    return StringUtils.trim(comments)
  }

  def setComments(comments: String) {
    this.comments = comments
  }

  def getSmallTalkCommentId: Long = {
    return smallTalkCommentId
  }

  def setSmallTalkCommentId(smallTalkCommentId: Long) {
    this.smallTalkCommentId = smallTalkCommentId
  }
}
