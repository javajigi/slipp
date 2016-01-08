package net.slipp.web.smalltalk

import javax.annotation.Resource
import javax.validation.Valid

import com.typesafe.scalalogging.LazyLogging
import net.slipp.domain.smalltalk.SmallTalkComment
import net.slipp.domain.user.SocialUser
import net.slipp.domain.smalltalk.SmallTalk
import net.slipp.service.smalltalk.SmallTalkService
import net.slipp.support.web.argumentresolver.LoginUser
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.{PathVariable, RequestMapping, RequestMethod, ResponseBody}

@Controller
class SmallTalkContoller(@Resource(name = "smallTalkService") smallTalkService: SmallTalkService) extends LazyLogging {
  @RequestMapping(value = Array("/smalltalks"), method = Array(RequestMethod.POST))
  @ResponseBody def save(@LoginUser loginUser: SocialUser, @Valid smallTalk: SmallTalk, result: BindingResult) = {
    if (result.hasErrors()) {
      throw new IllegalAccessException
    }

    try {
      smallTalk.setWriter(loginUser)
      smallTalkService.create(smallTalk)
      "OK"
    } catch {
      case e: Exception => {
        logger.error("SmallTalk 데이터를 저장하는 중 오류. [Form] : {}", smallTalk, e)
        "FAIL"
      }
    }
  }

  @RequestMapping(Array("/ajax/smalltalks"))
  def finds(model: Model) = {
    model.addAttribute("smallTalks", smallTalkService.getLastTalks)
    "async/smalltalk"
  }

  @RequestMapping(value = Array("/ajax/smalltalks/{id}/comments"), method = Array(RequestMethod.POST))
  @ResponseBody def saveComment(@LoginUser loginUser: SocialUser, @PathVariable id: Long, smallTalkComment: SmallTalkComment) = {
    try {
      logger.debug("Comments : {}", smallTalkComment)
      smallTalkComment.setWriter(loginUser)
      smallTalkService.createComment(id, smallTalkComment)
    } catch {
      case e: Exception => {
        logger.error("SmallTalkComment 데이터를 저장하는 중 오류. [Form] : {}", smallTalkComment, e)
        new SmallTalkComment()
      }
    }
  }

  @RequestMapping(Array("/ajax/smalltalks/{id}/comments"))
  def getComments(@PathVariable id: Long, smallTalkComment: SmallTalkComment, model: Model) = {
    try {
      logger.debug("Comments : {}", smallTalkComment)
      model.addAttribute("smalltalkComments", smallTalkService.findCommnetsBySmallTalkId(id))
      "async/smalltalkcomments"
    } catch {
      case e: Exception => {
        logger.error("SmallTalkComment 데이터를 저장하는 중 오류. [Form] : {}", smallTalkComment, e)
        "async/smalltalkcomments"
      }
    }
  }
  
  def this() = this(null)
}