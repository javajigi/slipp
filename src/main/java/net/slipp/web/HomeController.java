package net.slipp.web;

import javax.annotation.Resource;

import net.slipp.domain.qna.QnaService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @Resource(name = "qnaService")
    private QnaService qnaService;
    
    @RequestMapping("")
    public String index(Model model) {
        model.addAttribute("questions", qnaService.findsQuestion());
        model.addAttribute("tags", qnaService.findsTag());
        return "qna/list";
    }

    @RequestMapping("/login")
    public String login(Model model) {
        return "login";
    }
}
