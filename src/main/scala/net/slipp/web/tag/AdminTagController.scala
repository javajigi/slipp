package net.slipp.web.tag

import java.lang.Long

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import com.typesafe.scalalogging.LazyLogging
import javax.annotation.Resource
import net.slipp.service.tag.TagService
import net.slipp.web.QnAPageableHelper.createPageableTagId
import org.springframework.web.bind.annotation.RequestMethod

@Controller
@RequestMapping(Array("/admin"))
class AdminTagController(
  @Resource(name = "tagService") tagService: TagService) extends LazyLogging {
  private val DefaultPageNo = 1
  private val DefaultPageSize = 20

  @RequestMapping(Array("/tags"))
  def tags(page: Integer, model: Model) = {
    tagsToModel(page, model)
    "admin/tags"
  }

  private def tagsToModel(page: Integer, model: Model) {
    model.addAttribute("tags", tagService.findAllTags(createPageableTagId(page, DefaultPageSize)))
    model.addAttribute("parentTags", tagService.findPooledParentTags)
  }

  @RequestMapping(value = Array("/tags"), method = Array(RequestMethod.POST))
  def create(@RequestParam name: String, @RequestParam parentTag: java.lang.Long, model: Model): String = {
    logger.debug(s"create tag : $name, $parentTag")

    try {
      tagService.saveTag(name, Option(parentTag))
      "redirect:/admin/tags"
    } catch {
      case e: IllegalArgumentException => {
        model.addAttribute("errorMessage", e.getMessage)
        tagsToModel(DefaultPageNo, model)
        return "admin/tags"
      }
    }
  }

  @RequestMapping(value = Array("/moveNewTag"), method = Array(RequestMethod.POST))
  def moveNewTag(@RequestParam tagId: Long, @RequestParam parentTag: Long) = {
    logger.debug(s"move new tag : $tagId, $parentTag")
    tagService.moveToTag(tagId, Option(parentTag))
    "redirect:/admin/tags"
  }

  def this() = this(null)
}