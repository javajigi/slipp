package net.slipp.web.qna;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.slipp.domain.fb.FacebookComment;
import net.slipp.domain.user.SocialUser;
import net.slipp.service.qna.FacebookService;
import net.slipp.support.web.argumentresolver.LoginUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Controller
@RequestMapping("/api/facebooks")
public class FacebookController {
    private static Logger log = LoggerFactory.getLogger(FacebookController.class);
    
    @Resource(name = "facebookService")
    private FacebookService facebookService;
    
    @Resource(name = "freemarkerConfiguration")
    Configuration configuration;
    
    @RequestMapping(value="/{id}/comments", produces="text/plain;charset=UTF-8")
    public @ResponseBody String comments(@PathVariable Long id) throws Exception {
        log.debug("question id : {}", id);

        List<FacebookComment> fbComments = facebookService.findFacebookComments(id);
        Map<String, Object> params = Maps.newHashMap();
        params.put("comments", fbComments);
        
        String result = createTemplate("fbcomments.ftl", params);
        log.debug("result : {}", result);
        return result;
    }
    
    @RequestMapping(value="/groups", produces="text/plain;charset=UTF-8")
    public @ResponseBody String findGroups(@LoginUser SocialUser loginUser) throws Exception {
        Map<String, Object> params = Maps.newHashMap();
        params.put("groups", facebookService.findFacebookGroups(loginUser));
        String result = createTemplate("fbgroups.ftl", params);
        log.debug("result : {}", result);
        return result;
    }
    
    private String createTemplate(String fileName, Map<String, Object> params) {
        Template template;
        try {
            template = configuration.getTemplate(fileName);
        } catch (IOException e) {
            log.error(e.toString());
            return null;
        }

        try {
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, params);
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }
    }
}
