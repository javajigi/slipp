package net.slipp.web.qna;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.slipp.domain.tag.Tag;
import net.slipp.repository.tag.TagRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/tags")
public class TagController {
	private static final Logger logger = LoggerFactory.getLogger(TagController.class);
	
	@Resource(name = "tagRepository")
	private TagRepository tagRepository;
	
	@RequestMapping("/search")
	public @ResponseBody List<TagForm> searchByTagName(String name) {
		logger.debug("search tag by name : {}", name);
		List<Tag> searchedTags = tagRepository.findByNameLike(name + "%");
		List<TagForm> tags = new ArrayList<TagForm>();
		for (Tag tag : searchedTags) {
            tags.add(new TagForm(tag.getName()));
        }
		return tags;
	}
}
