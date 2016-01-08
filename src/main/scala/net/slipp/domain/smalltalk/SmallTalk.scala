package net.slipp.domain.smalltalk

import java.util.{Date, List}
import javax.persistence.{Column, Entity, EntityListeners, FetchType, ForeignKey, GeneratedValue, GenerationType, Id, JoinColumn, ManyToOne, OneToMany, Temporal, TemporalType, Transient}
import javax.validation.constraints.NotNull

import com.fasterxml.jackson.annotation.JsonIgnore
import net.slipp.domain.summary.SiteSummary
import net.slipp.domain.user.SocialUser
import net.slipp.support.jpa.{CreatedAndUpdatedDateEntityListener, HasCreatedAndUpdatedDate}
import net.slipp.support.utils.{SlippStringUtils, TimeUtils}
import org.apache.commons.lang3.StringUtils
import org.hibernate.validator.constraints.Length

@Entity
@EntityListeners(Array(classOf[CreatedAndUpdatedDateEntityListener]))
class SmallTalk extends HasCreatedAndUpdatedDate {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private var smallTalkId: Long = _

  @NotNull
  @Length(min = 1, max = 200)
  @Column(name = "talk", length = 255, nullable = false)
  private var talk: String = null

  @ManyToOne
  @JoinColumn(foreignKey = new ForeignKey(name = "fk_smalltalk_writer"))
  private var writer: SocialUser = null

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_date", nullable = false, updatable = false)
  private var createdDate: Date = null

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "updated_date", nullable = false)
  private var updatedDate: Date = null

  @Transient private var siteSummary: SiteSummary = null
  @JsonIgnore
  @OneToMany(mappedBy = "smallTalk", fetch = FetchType.LAZY)
  private var smallTalkComments: List[SmallTalkComment] = null

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

  def getTalk: String = {
    return talk
  }

  def setTalk(talk: String) {
    this.talk = talk
  }

  def getWriter: SocialUser = {
    return writer
  }

  def setWriter(writer: SocialUser) {
    this.writer = writer
  }

  def getTime: String = {
    if (TimeUtils.diffDay(new Date, this.createdDate) == 0) {
      return TimeUtils.agoTime(this.createdDate)
    }
    return "아주 오래전..."
  }

  def getSiteSummary: SiteSummary = {
    return siteSummary
  }

  def setSiteSummary(siteSummary: SiteSummary) {
    this.siteSummary = siteSummary
  }

  def getUrlInTalk: String = {
    return SlippStringUtils.getUrlInText(getTalk)
  }

  def hasUrl: Boolean = {
    return !StringUtils.isBlank(getUrlInTalk)
  }

  override def toString: String = {
    val builder: StringBuilder = new StringBuilder
    builder.append("SmallTalk [smallTalkId=")
    builder.append(getSmallTalkId)
    builder.append(", talk=")
    builder.append(talk)
    builder.append(", writer=")
    builder.append(writer)
    builder.append(", createdDate=")
    builder.append(createdDate)
    builder.append(", updatedDate=")
    builder.append(updatedDate)
    builder.append(", siteSummary=")
    builder.append(siteSummary)
    builder.append("]")
    return builder.toString
  }

  def getSmallTalkComments: List[SmallTalkComment] = {
    return smallTalkComments
  }

  def setSmallTalkComments(smallTalkComments: List[SmallTalkComment]) {
    this.smallTalkComments = smallTalkComments
  }

  def getSmallTalkId: Long = {
    return smallTalkId
  }

  def setSmallTalkId(smallTalkId: Long) {
    this.smallTalkId = smallTalkId
  }
}
