package net.slipp.web.tag

import javax.annotation.Resource
import net.slipp.domain.tag.Tag
import net.slipp.service.tag.TagService
import net.slipp.web.QnAPageableHelper._
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import com.typesafe.scalalogging.LazyLogging
import org.springframework.web.bind.annotation.RequestParam

@RequestMapping(Array("/admin"))
class NAdminTagController(
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
    model.addAttribute("parentTags", tagService.findPooledParentTags())
  }

  @RequestMapping(value = Array("/tags"), method = Array(RequestMethod.POST))
  def create(
      @RequestParam name: String, 
      @RequestParam(required = false, defaultValue = "0") parentTag: Long, 
      model: Model): String = {
    logger.debug(s"create tag : $name, $parentTag")

    val originalTag = tagService.findTagByName(name)
    if (originalTag != null) {
      model.addAttribute("errorMessage", name + " 태그는 이미 존재합니다.")
      tagsToModel(DefaultPageNo, model)
      return "admin/tags"
    }

    if (parentTag == 0L) {
      tagService.saveTag(Tag.pooledTag(name))
      return "redirect:/admin/tags"
    }

    val parent = tagService.findTagById(parentTag)
    if (parent == null) {
      model.addAttribute("errorMessage", parentTag + " 태그 아이디는 존재하지 않습니다.")
      tagsToModel(DefaultPageNo, model)
      return "admin/tags"
    }

    tagService.saveTag(Tag.pooledTag(name, parent))
    return "redirect:/admin/tags"
  }

  @RequestMapping(value = Array("/moveNewTag"), method = Array(RequestMethod.POST))
  def moveNewTag(
      tagId: Long, 
      @RequestParam(required = false, defaultValue = "0") parentTag: Long) = {
    logger.debug(s"move new tag : $tagId, $parentTag")
    if (parentTag == 0L) tagService.moveToTag(tagId, null) else tagService.moveToTag(tagId, parentTag)
    "redirect:/admin/tags"
  }

  def this() = this(null)
}