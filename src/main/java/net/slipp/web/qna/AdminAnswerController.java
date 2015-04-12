package net.slipp.web.qna;

import javax.annotation.Resource;

import net.slipp.domain.qna.Answer;
import net.slipp.domain.user.SocialUser;
import net.slipp.service.qna.QnaService;
import net.slipp.service.tag.TagService;
import net.slipp.support.web.argumentresolver.LoginUser;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin/questions/{questionId}/answers")
public class AdminAnswerController {
	@Resource(name = "qnaService")
	private QnaService qnaService;
	
	@Resource(name = "tagService")
	private TagService tagService;
	
	@RequestMapping(value = "{answerId}/form", method = RequestMethod.GET)
	public String updateForm(@LoginUser SocialUser loginUser, @PathVariable Long questionId, @PathVariable Long answerId, String searchTerm, Model model)
		throws Exception {
		Answer answer = qnaService.findAnswerById(answerId);
		model.addAttribute("question", qnaService.findByQuestionId(questionId));
		model.addAttribute("answer", answer);
		model.addAttribute("searchTerm", searchTerm);
		return "admin/qna/answer";
	}
	
	@RequestMapping(value = "{answerId}", method = RequestMethod.PUT)
	public String update(@LoginUser SocialUser loginUser, @PathVariable Long questionId, @PathVariable Long answerId, Answer answer) throws Exception {
		Answer originalAnswer = qnaService.findAnswerById(answerId);
		qnaService.updateAnswer(originalAnswer.getWriter(), answer);
		return String.format("redirect:/admin/questions/%d#answer-%d", questionId, answerId);
	}
}
