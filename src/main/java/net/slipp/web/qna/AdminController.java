package net.slipp.web.qna;

import javax.annotation.Resource;

import net.slipp.domain.qna.Tag;
import net.slipp.repository.qna.NewTagRepository;
import net.slipp.repository.qna.TagRepository;

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

	@Resource(name = "tagRepository")
	private TagRepository tagRepository;
	
	@Resource(name = "newTagRepository")
	private NewTagRepository newTagRepository;

	@RequestMapping(value = "/tags", method = RequestMethod.GET)
	public String tags(Integer page, ModelMap model) throws Exception {
		model.addAttribute("tags", tagRepository.findAll(createPageable(page)));
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
		Tag originalTag = tagRepository.findByName(name);
		if (originalTag != null) {
			model.addAttribute("errorMessage", name + " 태그는 이미 존재합니다.");
			return tags(1, model);
		}
				
		tagRepository.save(new Tag(name));
		return "redirect:/admin/tags";
	}
	
	@RequestMapping(value = "/newtags", method = RequestMethod.GET)
	public String newTags(Integer page, ModelMap model) throws Exception {
		model.addAttribute("newtags", newTagRepository.findAll(createPageable(page)));
		return "admin/newtags";
	}
}
