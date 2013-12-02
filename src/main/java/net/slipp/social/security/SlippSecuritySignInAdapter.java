package net.slipp.social.security;

import javax.annotation.Resource;

import net.slipp.domain.qna.TemporaryAnswer;
import net.slipp.domain.user.SocialUser;
import net.slipp.service.user.SocialUserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;

public class SlippSecuritySignInAdapter implements SignInAdapter {
    private static Logger log = LoggerFactory.getLogger(SlippSecuritySignInAdapter.class);
    
	public final static String SIGN_IN_DETAILS_SESSION_ATTRIBUTE_NAME = "net.slipp.social.security.signInDetails";

	@Resource(name = "socialUserService")
	private SocialUserService socialUserService;

	public String signIn(String localUserId, Connection<?> connection, NativeWebRequest nativeWebRequest) {
		ConnectionKey connectionKey = connection.getKey();
		SocialUser socialUser = socialUserService.findByUserIdAndConnectionKey(localUserId, connectionKey);
		nativeWebRequest.setAttribute(SIGN_IN_DETAILS_SESSION_ATTRIBUTE_NAME, socialUser,
				RequestAttributes.SCOPE_SESSION);
		TemporaryAnswer temporaryAnswer = getTemporaryAnswer(nativeWebRequest);
		return SlippSecurityAuthenticationFilter.DEFAULT_AUTHENTICATION_URL + "?redirect=" + temporaryAnswer.generateUrl();
	}

    private TemporaryAnswer getTemporaryAnswer(NativeWebRequest nativeWebRequest) {
        Object value = nativeWebRequest.getAttribute(TemporaryAnswer.TEMPORARY_ANSWER_KEY, RequestAttributes.SCOPE_SESSION);
		log.debug("Temporary Answer : {}", value);
		if (value == null) {
		    return TemporaryAnswer.EMPTY_ANSWER;
		}
        return (TemporaryAnswer)value;
    }
}
