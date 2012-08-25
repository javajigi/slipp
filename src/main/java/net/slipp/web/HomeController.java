package net.slipp.web;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.slipp.domain.wiki.WikiPage;
import net.slipp.domain.wiki.WikiService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	@Resource(name="wikiService")
	private WikiService wikiService;
	
	@RequestMapping("/")
	public String home(Model model) {
		List<WikiPage> pages = wikiService.findWikiPages();
		model.addAttribute("pages", pages);
		return "index";
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
    public String login(Model model) {
        return "login";
    }
}
