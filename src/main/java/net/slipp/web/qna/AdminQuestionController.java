package net.slipp.web.qna;

import static net.slipp.web.QnAPageableHelper.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import net.slipp.domain.qna.Question;
import net.slipp.domain.qna.QuestionDto;
import net.slipp.service.qna.QnaService;
import net.slipp.service.tag.TagService;
import net.slipp.web.UserForm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin/questions")
public class AdminQuestionController {
	private static final Logger log = LoggerFactory.getLogger(AdminQuestionController.class);
	
	private static final int DEFAULT_PAGE_SIZE = 30;
	
	@Resource(name = "qnaService")
	private QnaService qnaService;
	
	@Resource(name = "tagService")
	private TagService tagService;
	
	@RequestMapping("")
	public String index(Integer page, String searchTerm, Model model) {
		log.debug("search keyword : " + searchTerm);
		page = revisedPage(page);
		model.addAttribute("questions", qnaService.findsAllQuestion(searchTerm, createPageableByQuestionUpdatedDate(page, DEFAULT_PAGE_SIZE)));
		model.addAttribute("searchTerm", searchTerm);
		return "admin/qna/list";
	}
	
	@RequestMapping("/{id}")
	public String show(@PathVariable Long id, String searchTerm, Model model, HttpSession session) {
	    Question question = qnaService.showQuestion(id);
		model.addAttribute("question", question);
		model.addAttribute("tags", tagService.findLatestTags());
		model.addAttribute("user", new UserForm());
		model.addAttribute("searchTerm", searchTerm);
		return "admin/qna/show";
	}	
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public String delete(@PathVariable Long id, String searchTerm) {
		Question question = qnaService.findByQuestionId(id);
		qnaService.deleteQuestion(question.getWriter(), id);
		return String.format("redirect:/admin/questions?searchTerm=%s", searchTerm);
	}
	
	@RequestMapping("/{id}/form")
	public String updateForm(@PathVariable Long id, String searchTerm, Model model) {
		Question question = qnaService.findByQuestionId(id);
		model.addAttribute("question", question);
		model.addAttribute("searchTerm", searchTerm);
		return "admin/qna/form";
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public String update(@PathVariable Long id, String searchTerm, QuestionDto updatedQuestion) {
		log.debug("Question : {}", updatedQuestion);

		Question originalQuestion = qnaService.findByQuestionId(id);
		qnaService.updateQuestionByAdmin(originalQuestion.getWriter(), updatedQuestion);
		return String.format("redirect:/admin/questions/%d?searchTerm=%s", id, searchTerm);
	}
}
