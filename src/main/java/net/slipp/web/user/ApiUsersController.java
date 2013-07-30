package net.slipp.web.user;

import javax.annotation.Resource;

import net.slipp.domain.user.SocialUser;
import net.slipp.service.user.SocialUserService;
import net.slipp.support.web.argumentresolver.LoginUser;

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
    public @ResponseBody
    boolean duplicateUserId(@LoginUser(required = false) SocialUser loginUser, String userId) {
        log.debug("userId : {}", userId);

        SocialUser socialUser = userService.findByUserId(userId);
        return checkDuplicate(loginUser, socialUser);
    }

    @RequestMapping("/duplicateEmail")
    public @ResponseBody
    boolean duplicateEmail(@LoginUser(required = false) SocialUser loginUser, String email) {
        log.debug("email : {}", email);

        SocialUser socialUser = userService.findByEmail(email);
        return checkDuplicate(loginUser, socialUser);
    }

    boolean checkDuplicate(SocialUser loginUser, SocialUser socialUser) {
        if (socialUser == null) {
            return false;
        }
        if (socialUser.isSameUser(loginUser)) {
            return false;
        }
        return true;
    }
}
