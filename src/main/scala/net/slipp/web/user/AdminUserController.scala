package net.slipp.web.user

import java.net.URLEncoder
import javax.annotation.Resource

import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

import net.slipp.service.qna.BlockService
import net.slipp.service.user.SocialUserService
import net.slipp.web.QnAPageableHelper.revisedPage

@Controller
@RequestMapping(Array("/admin/users"))
class AdminUserController(
  @Resource(name = "socialUserService") userService: SocialUserService,
  @Resource(name = "blockService") blockService: BlockService) {
  private val log = LoggerFactory.getLogger(classOf[AdminUserController])
  private val DefaultPageNo = 1
  private val DefaultPageSize = 50

  @RequestMapping(Array(""))
  def users(page: Integer, searchTerm: String, model: Model) = {
    model.addAttribute("users", userService.findsUser(searchTerm, createPageable(page)))
    model.addAttribute("searchTerm", searchTerm)
    "admin/users"
  }

  private def createPageable(page: Integer) = {
    val sort = new Sort(Direction.DESC, "id")
    new PageRequest(revisedPage(page, DefaultPageNo) - 1, DefaultPageSize, sort)
  }

  @RequestMapping(value = Array("/{id}/resetpassword"), method = Array(RequestMethod.POST))
  def resetPassword(@PathVariable id: Long, page: Integer) = {
    log.debug("Id : {}, Page Number : {}", id, page)

    userService.resetPassword(userService.findById(id))
    "redirect:/admin/users?page=%d".format(page + 1)
  }

  @RequestMapping(value = Array("/{id}/block"), method = Array(RequestMethod.POST))
  def block(@PathVariable id: Long, page: Integer, searchTerm: String) = {
    blockService.block(id);
    "redirect:/admin/users?page=%d&searchTerm=%s".format(page + 1, URLEncoder.encode(searchTerm, "UTF-8"));
  }
  
  def this() = this(null, null)
}