package net.slipp.web

import java.util.Date
import java.util.List
import javax.annotation.Resource

import net.slipp.domain.wiki.WikiPage
import net.slipp.service.qna.QnaService
import net.slipp.service.smalltalk.SmallTalkService
import net.slipp.service.tag.TagService
import net.slipp.service.wiki.WikiService
import net.slipp.web.QnAPageableHelper._

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.slf4j.LoggerFactory

@Controller
class HomeController(
  @Value("#{applicationProperties['environment']}") environment: String,
  @Resource(name="wikiService") wikiService: WikiService,
  @Resource(name = "qnaService") qnaService: QnaService,
  @Resource(name = "tagService") tagService: TagService) {
  private val logger = LoggerFactory.getLogger(classOf[HomeController])
  private val DefaultPageNo = 1
  private val DefaultPageSize = 5
  
  @RequestMapping(Array("/"))
  def home(model: Model) = {
    productionMode(model);
    model.addAttribute("questions", qnaService.findsQuestion(createPageableByQuestionUpdatedDate(DefaultPageNo, DefaultPageSize)));
    model.addAttribute("tags", tagService.findLatestTags());    
    "index";
  }
  
  private def productionMode(model: Model) {
    if (isProductionMode()) {
      model.addAttribute("pages", wikiService.findWikiPages());     
    }else{
      model.addAttribute("pages", wikiService.findDummyWikiPages());
    }
  }
  
  private def isProductionMode() = {
    logger.debug("environment : {}", environment)
    "PRODUCTION".equals(environment);
  }
  
  @RequestMapping(Array("/rss"))
  def rss(model: Model) = {
    model.addAttribute("pages", wikiService.findWikiPages());
    model.addAttribute("now", new Date());
    "rss";
  }
  
  @RequestMapping(Array("/code"))
  def code() = "code"
  
  @RequestMapping(Array("/about"))
  def about = "about"

  @RequestMapping(Array("/blank"))
  def blank = "blank"
 
  @RequestMapping(Array("/google528d99e4fd7e1630.html"))
  def googleVerification = "verification"
  
  def this() = this(null, null, null, null)
}