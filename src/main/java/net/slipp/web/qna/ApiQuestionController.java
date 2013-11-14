package net.slipp.web.qna;

import javax.servlet.http.HttpSession;

import net.slipp.domain.qna.TemporaryAnswer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/questions")
public class ApiQuestionController {
    private static Logger log = LoggerFactory.getLogger(ApiQuestionController.class);
    
    @RequestMapping("/{id}/answers/logoutuser")
    public @ResponseBody boolean answerWhenLogout(@PathVariable Long id, String contents, HttpSession session) {
        TemporaryAnswer answer = new TemporaryAnswer(id, contents);
        log.debug("TemporaryAnswer : {}", answer);
        session.setAttribute(TemporaryAnswer.TEMPORARY_ANSWER_KEY, answer);
        return true;
    }
}
