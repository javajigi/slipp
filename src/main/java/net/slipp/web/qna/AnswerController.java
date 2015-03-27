package net.slipp.web.qna;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import net.slipp.domain.qna.Answer;
import net.slipp.domain.qna.TemporaryAnswer;
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
@RequestMapping("/questions/{questionId}/answers")
public class AnswerController {
	private static final Logger logger = LoggerFactory.getLogger(AnswerController.class);

	@Resource(name = "qnaService")
	private QnaService qnaService;
	
	@Resource(name = "tagService")
	private TagService tagService;

	@RequestMapping(value = "", method = RequestMethod.POST)
	public String create(@LoginUser SocialUser loginUser, @PathVariable Long questionId, Answer answer, HttpSession session)
			throws Exception {
		logger.debug("questionId :{}, answer : {}", questionId, answer);
		qnaService.createAnswer(loginUser, questionId, answer);
		session.removeAttribute(TemporaryAnswer.TEMPORARY_ANSWER_KEY);
		return String.format("redirect:/questions/%d", questionId);
	}
	
	@RequestMapping(value = "{answerId}", method = RequestMethod.DELETE)
	public String delete(@LoginUser SocialUser loginUser, @PathVariable Long questionId, @PathVariable Long answerId)
			throws Exception {
		qnaService.deleteAnswer(loginUser, questionId, answerId);
		return String.format("redirect:/questions/%d", questionId);
	}
	
	@RequestMapping(value = "{answerId}/form", method = RequestMethod.GET)
	public String updateForm(@LoginUser SocialUser loginUser, @PathVariable Long questionId, @PathVariable Long answerId, Model model)
		throws Exception {
		Answer answer = qnaService.findAnswerById(answerId);
		if (!answer.isWritedBy(loginUser)) {
			throw new AccessDeniedException(loginUser.getUserId() + " is not owner!");
		}
		
		model.addAttribute("question", qnaService.findByQuestionId(questionId));
		model.addAttribute("answer", answer);
		model.addAttribute("tags", tagService.findLatestTags());
		return "qna/answer";
	}
	
	@RequestMapping(value = "{answerId}", method = RequestMethod.PUT)
	public String update(@LoginUser SocialUser loginUser, @PathVariable Long questionId, @PathVariable Long answerId, Answer answer) throws Exception {
		qnaService.updateAnswer(loginUser, answer);
		return String.format("redirect:/questions/%d#answer-%d", questionId, answerId);
	}
}
