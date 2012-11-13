package net.slipp.web.qna;

import javax.annotation.Resource;

import net.slipp.domain.qna.Answer;
import net.slipp.domain.user.SocialUser;
import net.slipp.service.qna.QnaService;
import net.slipp.support.web.argumentresolver.LoginUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/questions/{questionId}/answers")
public class AnswerController {
	private static final Logger logger = LoggerFactory.getLogger(AnswerController.class);

	@Resource(name = "qnaService")
	private QnaService qnaService;

	@RequestMapping(value = "", method = RequestMethod.POST)
	public String create(@LoginUser SocialUser loginUser, @PathVariable Long questionId, Answer answer)
			throws Exception {
		logger.debug("questionId :{}, answer : {}", questionId, answer);
		qnaService.createAnswer(loginUser, questionId, answer);
		return "redirect:/questions/" + questionId;
	}

	@RequestMapping(value = "{answerId}", method = RequestMethod.DELETE)
	public String delete(@LoginUser SocialUser loginUser, @PathVariable Long questionId, @PathVariable Long answerId)
			throws Exception {
		qnaService.deleteAnswer(loginUser, questionId, answerId);
		return "redirect:/questions/" + questionId;
	}
	
	@RequestMapping(value = "/{answerId}/like", method = RequestMethod.POST)
	public String like(@LoginUser SocialUser loginUser, @PathVariable Long questionId, @PathVariable Long answerId)
			throws Exception {
		qnaService.likeAnswer(loginUser, answerId);
		return "redirect:/questions/" + questionId;
	}
}
