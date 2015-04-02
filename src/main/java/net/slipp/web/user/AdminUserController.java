package net.slipp.web.user;

import java.net.URLEncoder;

import javax.annotation.Resource;

import net.slipp.domain.user.SocialUser;
import net.slipp.service.qna.BlockService;
import net.slipp.service.user.SocialUserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {
	private static final Logger log = LoggerFactory.getLogger(AdminUserController.class);
	
	private static final int DEFAULT_PAGE_NO = 1;
	private static final int DEFAULT_PAGE_SIZE = 50;
	
    @Resource(name = "socialUserService")
    private SocialUserService userService;
    
    @Resource(name = "blockService")
    private BlockService blockService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String users(Integer page, String searchTerm, ModelMap model) throws Exception {
		model.addAttribute("users", userService.findsUser(searchTerm, createPageable(page)));
		model.addAttribute("searchTerm", searchTerm);
		return "admin/users";
	}

	Pageable createPageable(Integer page) {
		if (page == null) {
			page = DEFAULT_PAGE_NO;
		}
		Sort sort = new Sort(Direction.DESC, "id");
		Pageable pageable = new PageRequest(page - 1, DEFAULT_PAGE_SIZE, sort);
		return pageable;
	}
	
	@RequestMapping(value="/{id}/resetpassword", method = RequestMethod.POST)
	public String resetPassword(@PathVariable Long id, Integer page) throws Exception {
		log.debug("Id : {}, Page Number : {}", id, page);
		SocialUser socialUser = userService.findById(id);
		userService.resetPassword(socialUser);
		return String.format("redirect:/admin/users?page=%d", page + 1);
	}
	
	@RequestMapping(value="/{id}/block", method = RequestMethod.POST)
	public String block(@PathVariable Long id, Integer page, String searchTerm) throws Exception {
		log.debug("Id : {}, Page Number : {}, Search Term : {}", id, page, searchTerm);
		blockService.block(id);
		return String.format("redirect:/admin/users?page=%d&searchTerm=%s", page + 1, URLEncoder.encode(searchTerm, "UTF-8"));
	}
}
