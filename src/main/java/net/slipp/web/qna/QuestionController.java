package net.slipp.web.qna;

import static net.slipp.web.QnAPageableHelper.createPageableByQuestionUpdatedDate;
import static net.slipp.web.QnAPageableHelper.revisedPage;

import javax.annotation.Resource;

import net.slipp.domain.qna.Answer;
import net.slipp.domain.qna.Question;
import net.slipp.domain.qna.QuestionDto;
import net.slipp.domain.user.SocialUser;
import net.slipp.service.qna.QnaService;
import net.slipp.service.tag.TagService;
import net.slipp.support.web.argumentresolver.LoginUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/questions")
public class QuestionController {
	private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

	private static final int DEFAULT_PAGE_SIZE = 15;

	@Resource(name = "qnaService")
	private QnaService qnaService;
	
	@Resource(name = "tagService")
	private TagService tagService;
	
	@RequestMapping("")
	public String index(Integer page, Model model) {
		page = revisedPage(page);
		logger.debug("currentPage : {}", page);
		model.addAttribute("questions", qnaService.findsQuestion(createPageableByQuestionUpdatedDate(page, DEFAULT_PAGE_SIZE)));
		model.addAttribute("tags", tagService.findPooledTags());
		return "qna/list";
	}

	@RequestMapping("/form")
	public String createForm(@LoginUser SocialUser loginUser, String currentTag, Model model) {
	    logger.debug("currentTag : {}", currentTag);
	    
		model.addAttribute("question", new QuestionDto());
		model.addAttribute("tags", tagService.findPooledTags());
		return "qna/form";
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public String create(@LoginUser SocialUser loginUser, QuestionDto newQuestion) {
		logger.debug("Question : {}", newQuestion);

		Question question = qnaService.createQuestion(loginUser, newQuestion);
		return String.format("redirect:/questions/%s", question.getQuestionId());
	}

	@RequestMapping("/{id}/form")
	public String updateForm(@LoginUser SocialUser loginUser, @PathVariable Long id, Model model) {
		Question question = qnaService.findByQuestionId(id);
		if (!question.isWritedBy(loginUser)) {
			throw new AccessDeniedException(loginUser.getUserId() + " is not owner!");
		}
		model.addAttribute("question", question);
		model.addAttribute("tags", tagService.findPooledTags());
		return "qna/form";
	}

	@RequestMapping(value = "", method = RequestMethod.PUT)
	public String update(@LoginUser SocialUser loginUser, QuestionDto updatedQuestion) {
		logger.debug("Question : {}", updatedQuestion);

		Question question = qnaService.updateQuestion(loginUser, updatedQuestion);
		return String.format("redirect:/questions/%s", question.getQuestionId());
	}

	@RequestMapping("{id}")
	public String show(@PathVariable Long id, Model model) {
	    Question question = qnaService.showQuestion(id);
	    if (question.isDeleted()) {
	        throw new AccessDeniedException(id + " question is deleted.");
	    }
		model.addAttribute("question", question);
		model.addAttribute("answer", new Answer());
		model.addAttribute("tags", tagService.findPooledTags());
		return "qna/show";
	}
	
	@RequestMapping(value="{id}", method=RequestMethod.DELETE)
	public String delete(@LoginUser SocialUser loginUser, @PathVariable Long id) {
		qnaService.deleteQuestion(loginUser, id);
		return "redirect:/questions";
	}

	@RequestMapping("/tagged/{name}")
	public String listByTagged(@PathVariable String name, Integer page, Model model) {
		page = revisedPage(page);
		model.addAttribute("currentTag", tagService.findTagByName(name));
		model.addAttribute("questions", qnaService.findsByTag(name, createPageableByQuestionUpdatedDate(page, DEFAULT_PAGE_SIZE)));
		model.addAttribute("tags", tagService.findPooledTags());
		return "qna/list";
	}
}
