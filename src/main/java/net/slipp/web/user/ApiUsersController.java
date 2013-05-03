package net.slipp.web.user;

import javax.annotation.Resource;

import net.slipp.domain.user.SocialUser;
import net.slipp.service.user.SocialUserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/users")
public class ApiUsersController {
    private static Logger log = LoggerFactory.getLogger(ApiUsersController.class);
    
    @Resource(name = "socialUserService")
    private SocialUserService userService;
    
    @RequestMapping("/duplicateUserId")
    public @ResponseBody String login(String userId) {
        log.debug("userId : {}", userId);
        
        SocialUser socialUser = userService.findByUserId(userId);
        if (socialUser == null) {
            return "false";
        }
        return "true";
    }
}
