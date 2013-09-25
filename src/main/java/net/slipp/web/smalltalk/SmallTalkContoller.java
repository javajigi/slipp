package net.slipp.web.smalltalk;

import java.util.List;

import javax.annotation.Resource;

import net.slipp.domain.smalltalk.SmallTalk;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

@Controller
public class SmallTalkContoller {
	
	private Logger log = LoggerFactory.getLogger(MailService.class);
	
	@Resource(name = "smallTalkService")
	private SmallTalkService smallTalkService;

	@RequestMapping(value = "/smalltalks", method = RequestMethod.POST)
	public @ResponseBody
	String save(@LoginUser SocialUser loginUser,
			@Validated SmallTalk smallTalk, BindingResult result) {
		if (result.hasErrors()) {
			return "FAIL";
		}
		try {
			smallTalk.setWriter(loginUser);
			smallTalkService.save(smallTalk);
		}catch (Exception e) {
			log.error("SmallTalk 데이터를 저장하는 중 오류.", e);
			return "FAIL";
		}
		return "OK";
	}

	@RequestMapping(value = "/smalltalks", method = RequestMethod.GET)
	public @ResponseBody
	List<SmallTalk> find() {
		List<SmallTalk> smallTalks = Lists.newArrayList();
		try {
			smallTalks = smallTalkService.getLastTalks();
		}catch (Exception e) {
			log.error("SmallTalk 데이터를 가져오는 중 오류.", e);
		}
		return smallTalks;
	}

	@RequestMapping(value = "/ajax/smalltalks", method = RequestMethod.GET)
	public String finds(Model model){
		List<SmallTalk> smallTalks = Lists.newArrayList();
		try {
			smallTalks = smallTalkService.getLastTalks();
		}catch (Exception e) {
			log.error("SmallTalk 데이터를 가져오는 중 오류.", e);
		}
		model.addAttribute("smallTalks", smallTalks);
		return "async/smalltalk";
	}
}
