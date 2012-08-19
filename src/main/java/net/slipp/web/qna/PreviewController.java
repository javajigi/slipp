package net.slipp.web.qna;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/wikis")
public class PreviewController {
	private static final Logger logger = LoggerFactory.getLogger(PreviewController.class);
	
	@RequestMapping("/preview")
	public String preview(String data, Model model) throws Exception {
		logger.debug("contents : {}", data);
		model.addAttribute("contents", data);
		return "qna/preview";
	}
}
