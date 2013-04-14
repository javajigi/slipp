package net.slipp.web;

import static net.slipp.web.QuestionPageableHelper.*;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.slipp.domain.wiki.WikiPage;
import net.slipp.service.qna.QnaService;
import net.slipp.service.tag.TagService;
import net.slipp.service.wiki.WikiService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	private static final int DEFAULT_PAGE_NO = 1;
	private static final int DEFAULT_PAGE_SIZE = 5;	
	
	@Value("#{applicationProperties['environment']}")
	private String environment;
	
	@Resource(name="wikiService")
	private WikiService wikiService;
	
	@Resource(name = "qnaService")
	private QnaService qnaService;
	
	@Resource(name = "tagService")
	private TagService tagService;
	
	@RequestMapping("/")
	public String home(Model model) {
		if (isProductionMode()) {
			model.addAttribute("pages", wikiService.findWikiPages());			
		}
		model.addAttribute("questions", qnaService.findsQuestion(createPageable(DEFAULT_PAGE_NO, DEFAULT_PAGE_SIZE)));
		model.addAttribute("tags", tagService.findPooledTags());		
		return "index";
	}

	private boolean isProductionMode() {
		return "PRODUCTION".equals(environment);
	}
	
	@RequestMapping("/rss")
	public String rss(Model model) {
		List<WikiPage> pages = wikiService.findWikiPages();
		model.addAttribute("pages", pages);
		model.addAttribute("now", new Date());
		return "rss";
	}
	
	@RequestMapping("/code")
	public String code() {
		return "code";
	}
	
	@RequestMapping("/about")
	public String about() {
		return "about";
	}

    @RequestMapping("/login")
    public String login() {
        return "login";
    }
    
    @RequestMapping("/fblogout")
    public String logout() {
        return "fblogout";
    }
    
    @RequestMapping("/blank")
    public String blank() {
        return "blank";
    }
 
    @RequestMapping("/google528d99e4fd7e1630.html")
    public String googleVerification() {
        return "verification";
    }
}
