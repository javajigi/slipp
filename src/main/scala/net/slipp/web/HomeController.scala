package net.slipp.web

import java.util.Date
import javax.annotation.Resource

import net.slipp.service.qna.QnaService
import net.slipp.service.tag.TagService
import net.slipp.service.wiki.WikiService
import net.slipp.web.QnAPageableHelper._
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class HomeController(
  @Autowired env: Environment,
  @Resource(name="wikiService") wikiService: WikiService,
  @Resource(name = "qnaService") qnaService: QnaService,
  @Resource(name = "tagService") tagService: TagService) {
  private val logger = LoggerFactory.getLogger(classOf[HomeController])
  private val DefaultPageNo = 1
  private val DefaultPageSize = 5
  
  @RequestMapping(Array("/"))
  def home(model: Model) = {
    productionMode(model)
    model.addAttribute("questions", qnaService.findsQuestion(createPageableByQuestionUpdatedDate(DefaultPageNo, DefaultPageSize)))
    model.addAttribute("tags", tagService.findLatestTags)
    "index"
  }
  
  private def productionMode(model: Model) {
    if (isProductionMode()) model.addAttribute("pages", wikiService.findWikiPages)
    else model.addAttribute("pages", wikiService.findDummyWikiPages)
  }
  
  private def isProductionMode() = {
    val profile = env.getProperty("environment")
    logger.debug("environment : {}", profile)
    
    "PRODUCTION".equals(profile)
  }
  
  @RequestMapping(Array("/rss"))
  def rss(model: Model) = {
    model.addAttribute("pages", wikiService.findWikiPages)
    model.addAttribute("now", new Date())
    "rss"
  }
  
  @RequestMapping(Array("/code"))
  def code = "code"
  
  @RequestMapping(Array("/about"))
  def about = "about"

  @RequestMapping(Array("/blank"))
  def blank = "blank"
 
  @RequestMapping(Array("/google528d99e4fd7e1630.html"))
  def googleVerification = "verification"
  
  def this() = this(null, null, null, null)
}