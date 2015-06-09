package net.slipp.web.tag

import javax.annotation.Resource

import net.slipp.domain.tag.Tag
import net.slipp.service.tag.TagService

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

import com.typesafe.scalalogging.LazyLogging

@Controller
@RequestMapping(Array("/api/tags"))
class ApiTagsController(@Resource(name = "tagService") tagService: TagService) extends LazyLogging {
  @RequestMapping(Array("/duplicateTag"))
  @ResponseBody def duplicateTag(name: String) = {
    logger.debug(s"TagName $name")
    val tag = tagService.findTagByName(name)
    if (tag == null) false else true
  }

  def this() = this(null)
}