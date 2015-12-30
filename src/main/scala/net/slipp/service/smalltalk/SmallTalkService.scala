package net.slipp.service.smalltalk

import java.util.List
import javax.annotation.Resource
import net.slipp.domain.smalltalk.SmallTalkComment
import net.slipp.domain.summary.SiteSummary
import net.slipp.domain.user.SocialUser
import net.slipp.domain.smalltalk.SmallTalk
import net.slipp.repository.smalltalk.SmallTalkCommentRepository
import net.slipp.repository.smalltalk.SmallTalkRepository
import net.slipp.service.summary.SummaryService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import com.google.common.collect.Lists
import org.springframework.transaction.annotation.Transactional
import scala.collection.JavaConversions._

@Service("smallTalkService")
@Transactional class SmallTalkService {
  private var logger: Logger = LoggerFactory.getLogger(classOf[SmallTalkService])
  @Resource(name = "smallTalkRepository") private var smallTalkRepository: SmallTalkRepository = null
  @Resource(name = "smallTalkCommentRepository") private var smallTalkCommentRepository: SmallTalkCommentRepository = null
  @Resource(name = "summaryService") private var summaryService: SummaryService = null

  def create(smallTalk: SmallTalk) {
    logger.debug("SmallTalk : {}", smallTalk)
    smallTalkRepository.save(smallTalk)
  }

  def createComment(smallTalkId: Long, smallTalkComment: SmallTalkComment): SmallTalkComment = {
    val smallTalk: SmallTalk = smallTalkRepository.findOne(smallTalkId)
    smallTalkComment.commentTo(smallTalk)
    return smallTalkCommentRepository.save(smallTalkComment)
  }

  def getLastTalks: List[SmallTalk] = {
    val page: Page[SmallTalk] = smallTalkRepository.findAll(getPager)
    val orgSmallTalks: List[SmallTalk] = page.getContent
    val smallTalks = Lists.newArrayList[SmallTalk]
    for (smallTalk <- orgSmallTalks) {
      if (smallTalk.hasUrl) {
        smallTalk.setSiteSummary(findSummary(smallTalk))
      }
      smallTalks.add(smallTalk)
    }
    return smallTalks
  }

  private def findSummary(smallTalk: SmallTalk): SiteSummary = {
    return summaryService.findOneThumbnail(smallTalk.getUrlInTalk)
  }

  private def getPager: PageRequest = {
    return new PageRequest(0, 10, sortByIdDesc)
  }

  private def sortByIdDesc: Sort = {
    return new Sort(Sort.Direction.DESC, "smallTalkId")
  }

  def findCommnetsBySmallTalkId(smallTalkId: Long): List[SmallTalkComment] = {
    val smallTalk: SmallTalk = new SmallTalk
    smallTalk.setSmallTalkId(smallTalkId)
    return smallTalkCommentRepository.findBySmallTalk(smallTalk)
  }

  def deleteToBlock(user: SocialUser) {
    val comments: List[SmallTalkComment] = smallTalkCommentRepository.findByWriter(user)
    deleteSmallTalkComments(comments)
    val smallTalks: List[SmallTalk] = smallTalkRepository.findByWriter(user)
    for (smallTalk <- smallTalks) {
      val commentsAll: List[SmallTalkComment] = smallTalkCommentRepository.findBySmallTalk(smallTalk)
      deleteSmallTalkComments(commentsAll)
      smallTalkRepository.delete(smallTalk)
    }
  }

  private def deleteSmallTalkComments(comments: List[SmallTalkComment]) {
    import scala.collection.JavaConversions._
    for (comment <- comments) {
      smallTalkCommentRepository.delete(comment)
    }
  }
}
