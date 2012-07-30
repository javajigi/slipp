package net.slipp.web.qna;

import javax.annotation.Resource;

import net.slipp.domain.tag.Tag;
import net.slipp.domain.tag.TagService;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin")
public class AdminController {
	private static final int DEFAULT_PAGE_NO = 1;
	private static final int DEFAULT_PAGE_SIZE = 10;

	@Resource(name = "tagService")
	private TagService tagService;

	@RequestMapping(value = "/tags", method = RequestMethod.GET)
	public String tags(Integer page, ModelMap model) throws Exception {
		model.addAttribute("tags", tagService.findTags(createPageable(page)));
		return "admin/tags";
	}

	Pageable createPageable(Integer page) {
		if (page == null) {
			page = DEFAULT_PAGE_NO;
		}
		Sort sort = new Sort(Direction.DESC, "tagId");
		Pageable pageable = new PageRequest(page - 1, DEFAULT_PAGE_SIZE, sort);
		return pageable;
	}
	
	@RequestMapping(value = "/tags", method = RequestMethod.POST)
	public String create(String name, ModelMap model) throws Exception {
		Tag originalTag = tagService.findTagByName(name);
		if (originalTag != null) {
			model.addAttribute("errorMessage", name + " 태그는 이미 존재합니다.");
			return tags(1, model);
		}
				
		tagService.saveTag(new Tag(name));
		return "redirect:/admin/tags";
	}
	
	@RequestMapping(value = "/newtags", method = RequestMethod.GET)
	public String newTags(Integer page, ModelMap model) throws Exception {
		model.addAttribute("newtags", tagService.findNewTags(createPageable(page)));
		return "admin/newtags";
	}
	
	@RequestMapping(value = "/moveNewTag", method = RequestMethod.POST)
	public String moveNewTag(Long tagId) throws Exception {
		tagService.moveToTagPool(tagId);
		return "redirect:/admin/newtags";
	}
}
