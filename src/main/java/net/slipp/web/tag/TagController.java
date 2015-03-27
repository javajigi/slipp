package net.slipp.web.tag;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.slipp.domain.tag.Tag;
import net.slipp.service.qna.FacebookService;
import net.slipp.service.tag.TagService;
import net.slipp.service.user.SocialUserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
	
    @Resource(name = "facebookService")
    private FacebookService facebookService;
	
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
	
	Pageable createPageable(Integer page) {
		if (page == null) {
			page = DEFAULT_PAGE_NO;
		}
		Sort sort = new Sort(Direction.DESC, "tagId");
		Pageable pageable = new PageRequest(page - 1, DEFAULT_PAGE_SIZE, sort);
		return pageable;
	}
}
