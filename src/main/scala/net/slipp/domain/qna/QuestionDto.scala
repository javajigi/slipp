package net.slipp.domain.qna

import java.lang.Long
import java.util.{HashSet, Set}

import com.google.common.collect.Sets
import net.slipp.domain.fb.{EmptyFacebookGroup, FacebookGroup}
import org.apache.commons.lang3.StringUtils

import scala.beans.BeanProperty

object QuestionDto {
  private val FACEBOOK_GROUP_DELIMETER: String = "::"

  private[qna] def createFacebookGroups(fbGroups: Array[String]): Set[FacebookGroup] = {
    if (fbGroups == null || fbGroups.length == 0) {
      return new HashSet[FacebookGroup]
    }
    val groups: Set[FacebookGroup] = Sets.newHashSet[FacebookGroup]
    for (each <- fbGroups) {
      groups.add(createFacebookGroup(each))
    }
    return groups
  }

  private[qna] def createFacebookGroup(fbGroup: String): FacebookGroup = {
    if (StringUtils.isBlank(fbGroup)) {
      return new EmptyFacebookGroup
    }
    val parsedGroups: Array[String] = fbGroup.split(FACEBOOK_GROUP_DELIMETER)
    if (parsedGroups.length != 2) {
      return new EmptyFacebookGroup
    }
    return new FacebookGroup(parsedGroups(0), replaceSpaceToDash(parsedGroups(1)))
  }

  private[qna] def replaceSpaceToDash(groupName: String): String = {
    if (StringUtils.isBlank(groupName)) {
      return ""
    }
    return groupName.replaceAll(" ", "-")
  }
}

class QuestionDto(qId: Long, t: String, c: String, pTags: String) {
  @BeanProperty
  var questionId: Long = qId

  @BeanProperty
  var title: String = t

  @BeanProperty
  var contents: String = c

  @BeanProperty
  var plainTags: String = pTags

  @BeanProperty
  var connected: Boolean = false

  @BeanProperty
  var plainFacebookGroups: Array[String] = null

  @BeanProperty
  var originalAnswerId: Long = _

  @BeanProperty
  var moveAnswers: Array[Long] = _

  def this(title: String, contents: String, plainTags: String) = this(null, title, contents, plainTags)

  def this(questionId: Long, originalAnswerId: Long, contents: String) = {
    this(questionId, null, contents, null)
    this.originalAnswerId = originalAnswerId
  }

  def this() = this(null, null, null, null)

  def getFacebookGroups: Set[FacebookGroup] = {
    return QuestionDto.createFacebookGroups(this.plainFacebookGroups)
  }

  override def toString = s"QuestionDto($questionId, $title, $contents, $plainTags, $connected, $plainFacebookGroups, $originalAnswerId, $moveAnswers)"
}
