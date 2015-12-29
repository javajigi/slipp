package net.slipp.service.tag

import java.lang.Long

import java.util.{ArrayList, List, Set, StringTokenizer}
import javax.annotation.Resource

import com.google.common.collect.{Lists, Sets}
import net.slipp.domain.fb.FacebookGroup
import net.slipp.domain.qna.Question
import net.slipp.domain.tag.{Tag, TaggedType}
import net.slipp.domain.user.SocialUser
import net.slipp.ndomain.tag.TaggedHistory
import net.slipp.repository.tag.{TagRepository, TaggedHistoryRepository}
import net.slipp.service.MailService
import org.apache.commons.lang3.StringUtils
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.{Page, PageRequest, Pageable}
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.Assert

import scala.collection.JavaConversions._

object TagService {
  private[tag] def parseTags(plainTags: String): Set[String] = {
    val parsedTags = Sets.newHashSet[String]
    val tokenizer: StringTokenizer = new StringTokenizer(plainTags, " |,")
    while (tokenizer.hasMoreTokens) {
      parsedTags.add(tokenizer.nextToken)
    }
    return parsedTags
  }
}

@Service
@Transactional
class TagService {
  @Resource(name = "mailService") private var mailService: MailService = null
  @Resource(name = "tagRepository") private var tagRepository: TagRepository = null
  @Resource(name = "taggedHistoryRepository") private var taggedHistoryRepository: TaggedHistoryRepository = null

  def processTags(plainTags: String): Set[Tag] = {
    val parsedTags: Set[String] = TagService.parseTags(plainTags)
    val tags = Sets.newHashSet[Tag]
    for (each <- parsedTags) {
      val tag: Tag = tagRepository.findByName(each)
      if (tag == null) {
        tags.add(newTag(each))
      }
      else {
        tags.add(tag)
      }
    }
    return tags
  }

  def newTag(name: String): Tag = {
    val newTag: Tag = Tag.newTag(name)
    tagRepository.save(newTag)
    return newTag
  }

  private def getTagFromFacebookGroup(facebookGroup: FacebookGroup): Tag = {
    var tag = tagRepository.findByGroupId(facebookGroup.getGroupId)
    if (tag != null) {
      return tag
    }

    tag = tagRepository.findByName(facebookGroup.getName)
    if (tag != null) {
      tag.moveGroupTag(facebookGroup.getGroupId)
      return tag
    }

    val newTag: Tag = Tag.groupedTag(facebookGroup.getName, facebookGroup.getGroupId)
    tagRepository.save(newTag)
  }

  def processGroupTags(groupTags: Set[FacebookGroup]): Set[Tag] = {
    val tags = Sets.newHashSet[Tag]

    groupTags.foreach(each => {
      if (!each.isEmpty) {
        tags.add(getTagFromFacebookGroup(each))
      }
    })

    tags
  }

  def moveToTag(newTagId: Long, parentTagId: Option[Long]) {
    Assert.notNull(newTagId, "이동할 tagId는 null이 될 수 없습니다.")
    val parentTag: Tag = findParentTag(parentTagId)
    val tag: Tag = tagRepository.findOne(newTagId)
    tag.movePooled(parentTag)
  }

  private def findParentTag(parentTagId: Option[Long]): Tag = {
    parentTagId match {
      case Some(id) => tagRepository.findOne(id)
      case None => null
    }
  }

  def findAllTags(page: Pageable): Page[Tag] = {
    return tagRepository.findAll(page)
  }

  def findPooledParentTags: Iterable[Tag] = {
    return tagRepository.findPooledParents
  }

  @Cacheable(value = Array("latestTags"))
  def findLatestTags: List[Tag] = {
    val pageable: PageRequest = new PageRequest(0, 30)
    val page: Page[Array[AnyRef]] = taggedHistoryRepository.findsLatest(pageable)
    val latestTags = Lists.newArrayList[Tag]
    for (objects <- page) {
      val tag: Tag = findTagById(objects(0).asInstanceOf[Long])
      if (tag.isTagged) {
        latestTags.add(tag)
      }
    }
    return latestTags
  }

  def saveTag(name: String, parentTag: Long): Tag = {
    if (StringUtils.isBlank(name)) {
      throw new IllegalArgumentException("태그명을 입력해 주세요.")
    }
    val originalTag: Tag = tagRepository.findByName(name)
    if (originalTag != null) {
      throw new IllegalArgumentException(name + " 태그는 이미 존재합니다.")
    }
    var parent: Tag = null
    if (parentTag != null) {
      parent = tagRepository.findOne(parentTag)
    }
    return tagRepository.save(Tag.pooledTag(name, parent))
  }

  def findTagByName(name: String): Tag = {
    return tagRepository.findByName(name)
  }

  def findTagById(id: Long): Tag = {
    return tagRepository.findOne(id)
  }

  def findsBySearch(keyword: String): List[Tag] = {
    return tagRepository.findByNameLike(keyword + "%")
  }

  def saveTaggedHistories(question: Question, tags: Set[Tag]) {
    for (tag <- tags) {
      saveTaggedHistory(question.getWriter, question, tag, TaggedType.TAGGED)
    }
  }

  def saveTaggedHistory(loginUser: SocialUser, question: Question, tag: Tag, taggedType: TaggedType) {
    taggedHistoryRepository.save(new TaggedHistory(tag.getTagId, question.getQuestionId, loginUser.getId, taggedType))
  }
}
