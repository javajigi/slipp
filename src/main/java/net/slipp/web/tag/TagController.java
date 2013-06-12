package net.slipp.web.tag;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.slipp.domain.tag.Tag;
import net.slipp.domain.user.SocialUser;
import net.slipp.service.tag.TagService;
import net.slipp.service.user.SocialUserService;
import net.slipp.support.web.argumentresolver.LoginUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/tags")
public class TagController {
	private static final Logger logger = LoggerFactory.getLogger(TagController.class);
	
	private static final int DEFAULT_PAGE_NO = 1;
	private static final int DEFAULT_PAGE_SIZE = 20;
	
	@Resource(name = "socialUserService")
	private SocialUserService socialUserService;

	@Resource(name = "tagService")
	private TagService tagService;
	
	@RequestMapping("/form")
	public String createForm(Model model) {
	    model.addAttribute("tag", new TagForm());
	    return "tags/form";
	}
	
	@RequestMapping(value="", method=RequestMethod.POST)
	public String create(@LoginUser SocialUser loginUser, TagForm tag) {
		socialUserService.updateSlippUser(loginUser, tag.getEmail(), loginUser.getUserId());
		tagService.saveTag(tag.toTag(loginUser));
	    return "redirect:/tags/completed";
	}
	
	@RequestMapping("/create/completed")
	public String createCompleted() {
	    return "tags/completed";
	}
	
	@RequestMapping("/search")
	public @ResponseBody List<TagForm> searchByTagName(String name) {
		logger.debug("search tag by name : {}", name);
		List<Tag> searchedTags = tagService.findsBySearch(name);
		List<TagForm> tags = new ArrayList<TagForm>();
		for (Tag tag : searchedTags) {
            tags.add(new TagForm(tag.getName()));
        }
		return tags;
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String tags(Integer page, ModelMap model) throws Exception {
		model.addAttribute("tags", tagService.findPooledTags(createPageable(page)));
		model.addAttribute("parentTags", tagService.findPooledTags());
		return "tags/tags";
	}
	
	Pageable createPageable(Integer page) {
		if (page == null) {
			page = DEFAULT_PAGE_NO;
		}
		Sort sort = new Sort(Direction.DESC, "tagId");
		Pageable pageable = new PageRequest(page - 1, DEFAULT_PAGE_SIZE, sort);
		return pageable;
	}
}
