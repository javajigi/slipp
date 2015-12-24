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
import net.slipp.service.qna.AdminUserService
import net.slipp.service.user.SocialUserService
import net.slipp.web.QnAPageableHelper._
import com.typesafe.scalalogging.LazyLogging

@Controller
@RequestMapping(Array("/admin/users"))
class AdminUserController(
  @Resource(name = "socialUserService") userService: SocialUserService,
  @Resource(name = "adminUserService") adminUserService: AdminUserService) extends LazyLogging {
  private val DefaultPageNo = 1
  private val DefaultPageSize = 50

  @RequestMapping(Array(""))
  def users(page: Integer, searchTerm: String, model: Model) = {
    model.addAttribute("users", userService.findsUser(searchTerm, createPageableById(page, DefaultPageSize)))
    model.addAttribute("searchTerm", searchTerm)
    "admin/users"
  }

  @RequestMapping(value = Array("/{id}/resetpassword"), method = Array(RequestMethod.POST))
  def resetPassword(@PathVariable id: Long, page: Integer) = {
    logger.debug(s"Id : $id, Page Number : $page")

    userService.resetPassword(userService.findById(id))
    "redirect:/admin/users?page=%d".format(page + 1)
  }

  @RequestMapping(value = Array("/{id}/block"), method = Array(RequestMethod.POST))
  def block(@PathVariable id: Long, page: Integer, searchTerm: String) = {
    logger.debug(s"Id : $id, Page Number : $page, Search Term : $searchTerm")
    adminUserService.block(id);
    "redirect:/admin/users?page=%d&searchTerm=%s".format(page + 1, URLEncoder.encode(searchTerm, "UTF-8"));
  }

  @RequestMapping(value = Array("/{id}/admin"), method = Array(RequestMethod.POST))
  def admined(@PathVariable id: Long, page: Integer, searchTerm: String) = {
    logger.debug(s"Id : $id, Page Number : $page, Search Term : $searchTerm")
    adminUserService.admin(id);
    "redirect:/admin/users?page=%d&searchTerm=%s".format(page + 1, URLEncoder.encode(searchTerm, "UTF-8"));
  }

  @RequestMapping(value = Array("/{id}/unadmin"), method = Array(RequestMethod.POST))
  def unadmined(@PathVariable id: Long, page: Integer, searchTerm: String) = {
    logger.debug(s"Id : $id, Page Number : $page, Search Term : $searchTerm")
    adminUserService.unadmin(id);
    "redirect:/admin/users?page=%d&searchTerm=%s".format(page + 1, URLEncoder.encode(searchTerm, "UTF-8"));
  }

  def this() = this(null, null)
}