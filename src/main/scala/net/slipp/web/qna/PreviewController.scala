package net.slipp.web.qna

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import com.typesafe.scalalogging.LazyLogging

@Controller
@RequestMapping(Array("/wikis"))
class PreviewController extends LazyLogging {
  @RequestMapping(Array("/preview"))
  def preview(data: String, model: Model) = {
    logger.debug("contents : {}", data)
    model.addAttribute("contents", data)
    "qna/preview"
  }
}