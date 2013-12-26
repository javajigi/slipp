package net.slipp.web.smalltalk;

import javax.annotation.Resource;

import net.slipp.domain.smalltalk.SmallTalk;
import net.slipp.domain.smalltalk.SmallTalkComment;
import net.slipp.domain.user.SocialUser;
import net.slipp.service.MailService;
import net.slipp.service.smalltalk.SmallTalkService;
import net.slipp.support.web.argumentresolver.LoginUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SmallTalkContoller {

	private Logger log = LoggerFactory.getLogger(MailService.class);

	@Resource(name = "smallTalkService")
	private SmallTalkService smallTalkService;

	@RequestMapping(value = "/smalltalks", method = RequestMethod.POST)
	@ResponseBody
	public String save(@LoginUser SocialUser loginUser, @Validated SmallTalk smallTalk, BindingResult result) {
		try {
			if (result.hasErrors()) {
				throw new Exception();
			}
			smallTalk.setWriter(loginUser);
			smallTalkService.create(smallTalk);
		} catch (Exception e) {
			log.error("SmallTalk 데이터를 저장하는 중 오류. [Form] : {}", smallTalk, e);
			return "FAIL";
		}
		return "OK";
	}

	@RequestMapping(value = "/ajax/smalltalks", method = RequestMethod.GET)
	public String finds(Model model) {
		model.addAttribute("smallTalks", smallTalkService.getLastTalks());
		return "async/smalltalk";
	}
	
    @RequestMapping(value = "/ajax/smalltalks/{id}/comments", method = RequestMethod.POST)
    public @ResponseBody SmallTalkComment saveComment(@LoginUser SocialUser loginUser, @PathVariable Long id, SmallTalkComment smallTalkComment) {
        try {
            log.debug("Comments : {}", smallTalkComment);
            smallTalkComment.setWriter(loginUser);
            return smallTalkService.createComment(id, smallTalkComment);
        } catch (Exception e) {
            log.error("SmallTalkComment 데이터를 저장하는 중 오류. [Form] : {}", smallTalkComment, e);
            return new SmallTalkComment();
        }
    }
	
	@RequestMapping(value = "/ajax/smalltalks/{id}/comments", method = RequestMethod.GET)
	public String getComments(@PathVariable Long id, SmallTalkComment smallTalkComment, Model model) {
		try {
			log.debug("Comments : {}", smallTalkComment);
			model.addAttribute("smalltalkComments" ,smallTalkService.findCommnetsBySmallTalkId(id));
		} catch (Exception e) {
			log.error("SmallTalkComment 데이터를 저장하는 중 오류. [Form] : {}", smallTalkComment, e);
		}
		return "async/smalltalkcomments";
	}
}
