package net.slipp.web.tag;

import javax.annotation.Resource;

import net.slipp.domain.tag.Tag;
import net.slipp.service.tag.TagService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/tags")
public class ApiTagsController {
    @Resource(name="tagService")
    private TagService tagService;
    
    @RequestMapping("/duplicateTag")
    public @ResponseBody boolean duplicateTag(String name) {
        Tag tag = tagService.findTagByName(name);
        if (tag == null) {
            return false;
        }
        return true;
    }
}
