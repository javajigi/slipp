package net.slipp.web.tag;

import javax.annotation.Resource;

import net.slipp.domain.tag.Tag;
import net.slipp.service.tag.TagService;

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
public class AdminTagController {
	private static final int DEFAULT_PAGE_NO = 1;
	private static final int DEFAULT_PAGE_SIZE = 20;

	@Resource(name = "tagService")
	private TagService tagService;
	
	@RequestMapping(value = "/tags", method = RequestMethod.GET)
	public String tags(Integer page, ModelMap model) throws Exception {
		model.addAttribute("tags", tagService.findAllTags(createPageable(page)));
		model.addAttribute("parentTags", tagService.findPooledParentTags());
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
	public String create(String name, Long parentTag, ModelMap model) throws Exception {
		Tag originalTag = tagService.findTagByName(name);
		if (originalTag != null) {
			model.addAttribute("errorMessage", name + " 태그는 이미 존재합니다.");
			return tags(1, model);
		}
		
		if (parentTag == null) {
			tagService.saveTag(Tag.pooledTag(name));
			return "redirect:/admin/tags";
		}
		
		Tag parent = tagService.findTagById(parentTag);
		if (parent == null) {
			model.addAttribute("errorMessage", parentTag + " 태그 아이디는 존재하지 않습니다.");
			return tags(1, model);
		}
				
		tagService.saveTag(Tag.pooledTag(name, parent));
		return "redirect:/admin/tags";
	}
	
	@RequestMapping(value = "/moveNewTag", method = RequestMethod.POST)
	public String moveNewTag(Long tagId, Long parentTag) throws Exception {
		tagService.moveToTag(tagId, parentTag);
		return "redirect:/admin/tags";
	}
}
