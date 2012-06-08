package net.slipp.web.qna;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.slipp.domain.qna.Answer;
import net.slipp.domain.qna.QnaService;
import net.slipp.domain.qna.Question;
import net.slipp.domain.qna.Question_;
import net.slipp.domain.user.SocialUser;
import net.slipp.support.web.argumentresolver.LoginUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/questions")
public class QuestionController {
	private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

	private static final int DEFAULT_PAGE_NO = 1;
	private static final int DEFAULT_PAGE_SIZE = 1;

	@Resource(name = "qnaService")
	private QnaService qnaService;

	@RequestMapping("")
	public String index(Integer page, Model model) {
		page = revisedPage(page);
		logger.debug("currentPage : {}", page);
		model.addAttribute("questions", qnaService.findsQuestion(createPageable(page)));
		model.addAttribute("tags", qnaService.findsTag());
		return "qna/list";
	}

	private Pageable createPageable(Integer page) {
		Sort sort = new Sort(Direction.DESC, Question_.createdDate.getName());
		Pageable pageable = new PageRequest(page - 1, DEFAULT_PAGE_SIZE, sort);
		return pageable;
	}

	@RequestMapping("/form")
	public String createForm(HttpServletRequest request, Model model) {
		model.addAttribute(new Question());
		model.addAttribute("tags", qnaService.findsTag());
		return "qna/form";
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public String create(@LoginUser SocialUser user, Question question) {
		logger.debug("Question : {}", question);

		qnaService.createQuestion(user, question);
		return "redirect:/questions";
	}

	@RequestMapping("/{id}/form")
	public String updateForm(@PathVariable Long id, HttpServletRequest request, Model model) {
		model.addAttribute("question", qnaService.findByQuestionId(id));
		return "qna/form";
	}

	@RequestMapping(value = "", method = RequestMethod.PUT)
	public String update(@LoginUser SocialUser user, Question question) {
		logger.debug("Question : {}", question);

		qnaService.updateQuestion(user, question);
		return "redirect:/questions";
	}

	@RequestMapping("{id}")
	public String show(@PathVariable Long id, Model model) {
		model.addAttribute("question", qnaService.findByQuestionId(id));
		model.addAttribute("answer", new Answer());
		model.addAttribute("tags", qnaService.findsTag());
		return "qna/show";
	}

	@RequestMapping("/tagged/{name}")
	public String listByTagged(@PathVariable String name, Integer page, Model model) {
		page = revisedPage(page);
		model.addAttribute("questions", qnaService.findsByTag(name, createPageable(page)));
		model.addAttribute("tags", qnaService.findsTag());
		return "qna/list";
	}

	private Integer revisedPage(Integer page) {
		if (page == null) {
			page = DEFAULT_PAGE_NO;
		}
		return page;
	}
}
