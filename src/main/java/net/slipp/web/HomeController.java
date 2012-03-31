package net.slipp.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    private String getAuthenticatedUserName() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        return authentication == null ? null : authentication.getName();
    }

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("userName", getAuthenticatedUserName());
        model.addAttribute("securityLevel", "Public");
        return "index";
    }

    @RequestMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @RequestMapping("/protected")
    public String helloProtectedWorld(Model model) {
        model.addAttribute("userName", getAuthenticatedUserName());
        model.addAttribute("securityLevel", "Protected");
        return "helloWorld";
    }

}
