package net.slipp.web.user

import javax.annotation.Resource

import net.slipp.domain.user.SocialUser
import net.slipp.service.user.SocialUserService
import net.slipp.support.web.argumentresolver.LoginUser

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping(Array("/api/users"))
class ApiUsersController(
  @Resource(name = "socialUserService") userService: SocialUserService) {
  private val log = LoggerFactory.getLogger(classOf[ApiUsersController])

  @RequestMapping(Array("/duplicateUserId"))
  @ResponseBody def duplicateUserId(@LoginUser(required = false) loginUser: SocialUser, userId: String) = {
    log.debug("userId : {}", userId)
    checkDuplicate(loginUser, userService.findByUserId(userId))
  }

  @RequestMapping(Array("/duplicateEmail"))
  @ResponseBody def duplicateEmail(@LoginUser(required = false) loginUser: SocialUser, email: String) = {
    log.debug("email : {}", email)
    checkDuplicate(loginUser, userService.findByEmail(email))
  }

  def checkDuplicate(loginUser: SocialUser, socialUser: SocialUser): Boolean =
    if (socialUser == null) false else !socialUser.isSameUser(loginUser)

  def this() = this(null)
}