package net.slipp.web.qna;

import java.util.List;

import javax.annotation.Resource;

import net.slipp.domain.qna.Tag;
import net.slipp.repository.qna.TagRepository;

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
	public @ResponseBody List<Tag> searchByTagName(String name) {
		logger.debug("search tag by name : {}", name);
		return tagRepository.findByNameLike(name + "%");
	}
}
