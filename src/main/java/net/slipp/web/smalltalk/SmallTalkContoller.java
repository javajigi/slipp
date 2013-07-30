package net.slipp.web.smalltalk;

import java.util.List;

import javax.annotation.Resource;

import net.slipp.domain.smalltalk.SmallTalk;
import net.slipp.domain.user.SocialUser;
import net.slipp.service.smalltalk.SmallTalkService;
import net.slipp.support.web.argumentresolver.LoginUser;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SmallTalkContoller {

	@Resource(name = "smallTalkService")
	private SmallTalkService smallTalkService;

	@RequestMapping(value = "/smalltalks", method = RequestMethod.POST)
	public @ResponseBody
	String save(@LoginUser SocialUser loginUser, @Validated SmallTalk smallTalk, BindingResult result) {
		if(result.hasErrors()) {
			return "FAIL";
		}
		smallTalk.setWriter(loginUser);
		smallTalkService.save(smallTalk);
		return "OK";
	}
	@RequestMapping(value = "/smalltalks", method = RequestMethod.GET)
	public @ResponseBody List<SmallTalk> find(){
		return smallTalkService.getLastTalks().getContent();
	}
}
