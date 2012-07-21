package net.slipp.web.qna;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@RequestMapping(value="/tags", method=RequestMethod.GET)
	public String tags() throws Exception {
		return "admin/tags";
	}
}
