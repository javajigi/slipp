package net.slipp.web.qna;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/questions")
public class ApiQuestionController {
    public static final String TEMPORARY_ANSWER_KEY = "temporaryAnswer";
    private static Logger log = LoggerFactory.getLogger(ApiQuestionController.class);
    
    @RequestMapping("/answers/tosession")
    public @ResponseBody boolean setToSession(String contents, HttpSession session) {
        log.debug("temporary answer : {}", contents);
        
        session.setAttribute(TEMPORARY_ANSWER_KEY, contents);
        return true;
    }
}
