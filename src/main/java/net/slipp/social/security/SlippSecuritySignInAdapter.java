package net.slipp.social.security;

import javax.annotation.Resource;

import net.slipp.domain.user.SocialUser;
import net.slipp.domain.user.SocialUserService;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;

public class SlippSecuritySignInAdapter implements SignInAdapter {
	public final static String SIGN_IN_DETAILS_SESSION_ATTRIBUTE_NAME = "net.slipp.social.security.signInDetails";

	@Resource(name = "socialUserService")
	private SocialUserService socialUserService;

	public String signIn(String localUserId, Connection<?> connection, NativeWebRequest nativeWebRequest) {
		ConnectionKey connectionKey = connection.getKey();
		SocialUser socialUser = socialUserService.findByUserIdAndConnectionKey(localUserId, connectionKey);
		nativeWebRequest.setAttribute(SIGN_IN_DETAILS_SESSION_ATTRIBUTE_NAME, socialUser,
				RequestAttributes.SCOPE_SESSION);
		return null;
	}
}
