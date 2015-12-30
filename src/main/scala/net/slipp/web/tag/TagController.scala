package net.slipp.web.tag

import java.util.List
import javax.annotation.Resource
import scala.collection.JavaConversions._

import net.slipp.domain.tag.Tag
import net.slipp.service.qna.FacebookService
import net.slipp.service.tag.TagService
import net.slipp.service.user.SocialUserService
import net.slipp.web.QnAPageableHelper._
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import com.typesafe.scalalogging.LazyLogging

@Controller
@RequestMapping(Array("/tags"))
class TagController(@Resource(name = "tagService") tagService: TagService) extends LazyLogging {
  private val DefaultPageNo = 1
  private val DefaultPageSize = 20

  @RequestMapping(Array("/search"))
  @ResponseBody def searchByTagName(name: String): List[TagForm] = {
    logger.debug("search tag by name : {}", name);
    val searchedTags = tagService.findsBySearch(name);
    searchedTags.toList.map ( tag => new TagForm(tag.getName) )
  }
  
  def this() = this(null)
}