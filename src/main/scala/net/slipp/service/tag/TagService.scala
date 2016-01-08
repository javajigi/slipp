package net.slipp.service.tag

import java.util.Set
import javax.annotation.Resource

import com.google.common.collect.Lists
import net.slipp.domain.fb.FacebookGroup
import net.slipp.domain.qna.Question
import net.slipp.domain.tag.{TaggedHistory, Tag, TaggedType}
import net.slipp.domain.user.SocialUser
import net.slipp.repository.tag.{TagRepository, TaggedHistoryRepository}
import org.apache.commons.lang3.StringUtils
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.{Page, PageRequest, Pageable}
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.Assert

import scala.collection.JavaConversions._

object TagService {
  def parseTags(plainTags: String) = {
    plainTags.split(" |,").toSet
  }
}

@Service
@Transactional
class TagService {
  @Resource(name = "tagRepository") private var tagRepository: TagRepository = null
  @Resource(name = "taggedHistoryRepository") private var taggedHistoryRepository: TaggedHistoryRepository = null

  def processTags(plainTags: String): Set[Tag] = {
    val parsedTags = TagService.parseTags(plainTags)
    parsedTags.map(pTag => {
      Option(tagRepository.findByName(pTag)).getOrElse(newTag(pTag))
    })
  }

  def newTag(name: String) = {
    val newTag: Tag = Tag.newTag(name)
    tagRepository.save(newTag)
  }

  private def getTagByFacebookGroup(facebookGroup: FacebookGroup) = {
    Option(tagRepository.findByGroupId(facebookGroup.getGroupId))
      .orElse(Option(tagRepository.findByName(facebookGroup.getName))
        .map(t => t.moveGroupTag(facebookGroup.getGroupId)))
      .getOrElse(tagRepository.save(Tag.groupedTag(facebookGroup.getName, facebookGroup.getGroupId)))
  }

  def processGroupTags(groupTags: Set[FacebookGroup]): Set[Tag] = {
    groupTags.map(getTagByFacebookGroup)
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

  def findAllTags(page: Pageable) = tagRepository.findAll(page)

  def findPooledParentTags = tagRepository.findPooledParents

  @Cacheable(value = Array("latestTags"))
  def findLatestTags = {
    val pageable: PageRequest = new PageRequest(0, 30)
    val page: Page[Array[AnyRef]] = taggedHistoryRepository.findsLatest(pageable)
    val latestTags = Lists.newArrayList[Tag]
    for (objects <- page) {
      val tag: Tag = findTagById(objects(0).asInstanceOf[Long])
      if (tag.isTagged) {
        latestTags.add(tag)
      }
    }
    latestTags
  }

  def saveTag(name: String, parentTag: Option[Long]) = {
    if (StringUtils.isBlank(name)) {
      throw new IllegalArgumentException("태그명을 입력해 주세요.")
    }
    val originalTag = tagRepository.findByName(name)
    if (originalTag != null) {
      throw new IllegalArgumentException(name + " 태그는 이미 존재합니다.")
    }

    parentTag match {
      case Some(parentId) => tagRepository.save(Tag.pooledTag(name, tagRepository.findOne(parentId)))
      case None => tagRepository.save(Tag.pooledTag(name))
    }
  }

  def findTagByName(name: String) = tagRepository.findByName(name)

  def findTagById(id: Long) = tagRepository.findOne(id)

  def findsBySearch(keyword: String) = tagRepository.findByNameLike(keyword + "%")

  def saveTaggedHistories(question: Question, tags: Set[Tag]) {
    for (tag <- tags) {
      saveTaggedHistory(question.getWriter, question, tag, TaggedType.TAGGED)
    }
  }

  def saveTaggedHistory(loginUser: SocialUser, question: Question, tag: Tag, taggedType: TaggedType) {
    taggedHistoryRepository.save(new TaggedHistory(tag.getTagId, question.getQuestionId, loginUser.getId, taggedType))
  }
}