package net.slipp.web.qna

import java.io.IOException
import javax.annotation.Resource

import collection.JavaConversions._

import net.slipp.domain.fb.FacebookComment
import net.slipp.domain.user.SocialUser
import net.slipp.service.qna.FacebookService
import net.slipp.support.web.argumentresolver.LoginUser

import org.springframework.ui.freemarker.FreeMarkerTemplateUtils
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

import com.google.common.collect.Maps

import freemarker.template.Configuration
import freemarker.template.Template

import com.typesafe.scalalogging.LazyLogging

@RestController
@RequestMapping(Array("/api/facebooks"))
class FacebookController(
  @Resource(name = "facebookService") facebookService: FacebookService,
  @Resource(name = "freemarkerConfiguration") configuration: Configuration) extends LazyLogging {

  @RequestMapping(value = Array("/{id}/comments"), produces = Array("text/plain;charset=UTF-8"))
  def comments(@PathVariable id: Long) = {
    logger.debug("question id : {}", id.toString)

    val fbComments = facebookService.findFacebookComments(id)
    logger.debug("facebook comment : {}", fbComments)
    val result = createTemplate("fbcomments.ftl", Map("comments" -> fbComments))
    logger.debug("result : {}", result)
    result
  }

  @RequestMapping(value = Array("/groups"), produces = Array("text/plain;charset=UTF-8"))
  def findGroups(@LoginUser loginUser: SocialUser) = {
    val result = createTemplate("fbgroups.ftl", Map("groups" -> facebookService.findFacebookGroups(loginUser)))
    logger.debug("result : {}", result)
    result
  }

  private def createTemplate(fileName: String, params: java.util.Map[String, Object]) = {
    val template = configuration.getTemplate(fileName)
    FreeMarkerTemplateUtils.processTemplateIntoString(template, params)
  }
  
  def this() = this(null, null)
}