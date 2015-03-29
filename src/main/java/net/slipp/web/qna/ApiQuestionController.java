package net.slipp.web.qna;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import net.slipp.domain.qna.Answer;
import net.slipp.domain.qna.Question;
import net.slipp.domain.qna.TemporaryAnswer;
import net.slipp.domain.user.SocialUser;
import net.slipp.service.qna.QnaService;
import net.slipp.support.web.argumentresolver.LoginUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.restfb.util.StringUtils;

@Controller
@RequestMapping("/api/questions/{questionId}")
public class ApiQuestionController {
    private static Logger log = LoggerFactory.getLogger(ApiQuestionController.class);
    
    @Resource(name = "qnaService")
    private QnaService qnaService;
    
    @RequestMapping("answers/logoutuser")
    public @ResponseBody boolean answerWhenLogout(@PathVariable Long questionId, String contents, HttpSession session) {
        TemporaryAnswer answer = new TemporaryAnswer(questionId, contents);
        log.debug("TemporaryAnswer : {}", answer);
        session.setAttribute(TemporaryAnswer.TEMPORARY_ANSWER_KEY, answer);
        return true;
    }
    
    @RequestMapping(value = "/like", method = RequestMethod.POST)
    public @ResponseBody Integer likeQuestions(@LoginUser SocialUser loginUser, @PathVariable Long questionId)
            throws Exception {
        Question question = qnaService.likeQuestion(loginUser, questionId);
        return question.getSumLike();
    }  
    
	@RequestMapping(value="/detagged/{name}", method=RequestMethod.POST)
	public @ResponseBody boolean detagged(@LoginUser SocialUser loginUser, @PathVariable Long questionId, @PathVariable String name) {
		if (StringUtils.isBlank(name)) {
			return false;
		}
		qnaService.detagged(loginUser, questionId, name);
		return true;
	}
    
    @RequestMapping(value = "/answers/{answerId}/like", method = RequestMethod.POST)
    public @ResponseBody Integer likeAnswer(@LoginUser SocialUser loginUser, @PathVariable Long questionId, @PathVariable Long answerId)
            throws Exception {
        Answer answer = qnaService.likeAnswer(loginUser, answerId);
        return answer.getSumLike();
    }
    
    @RequestMapping(value = "/answers/{answerId}/dislike", method = RequestMethod.POST)
    public @ResponseBody Integer dislikeAnswer(@LoginUser SocialUser loginUser, @PathVariable Long questionId, @PathVariable Long answerId)
            throws Exception {
        Answer answer = qnaService.dislikeAnswer(loginUser, answerId);
        return answer.getSumDislike();
    }
}
