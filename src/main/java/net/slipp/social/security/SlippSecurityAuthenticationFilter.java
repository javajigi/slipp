package net.slipp.social.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.slipp.domain.user.SocialUser;
import net.slipp.support.security.SessionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.RememberMeServices;

public class SlippSecurityAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static Logger log = LoggerFactory.getLogger(SlippSecurityAuthenticationFilter.class);
    
	public final static String DEFAULT_AUTHENTICATION_URL = "/authenticate";

	protected SlippSecurityAuthenticationFilter() {
		super(DEFAULT_AUTHENTICATION_URL);
	}
	
	@Autowired
	private SessionService sessionService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		super.setAuthenticationManager(authenticationManager);
	}

	@Override
	public void setRememberMeServices(RememberMeServices rememberMeServices) {
		super.setRememberMeServices(rememberMeServices);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		log.debug("redirect url : {}", request.getAttribute("redirect"));
		
		if(sessionService.isAuthenticated()) {
		    log.debug("already authentication userId is : {}", sessionService.getAuthentication().getPrincipal());
			return sessionService.getAuthentication();
		} else {
			SocialUser signInDetails = (SocialUser) request.getSession().getAttribute(
					SlippSecuritySignInAdapter.SIGN_IN_DETAILS_SESSION_ATTRIBUTE_NAME);
			
			if (signInDetails == null) {
			    log.debug("sns login failed. so login anonymous!");
			    return new AnonymousAuthenticationToken("slippAnonymousAuthenticationToken", "anonymousUser", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
			}
			
			log.debug("sns login success. login userId : {}", signInDetails.getUserId());
			
			SlippUser userDetails;
			if (signInDetails.isSLiPPUser()) {
			    SlippUserDetailsService slippUserDetailsService = (SlippUserDetailsService)userDetailsService;
			    userDetails = (SlippUser)slippUserDetailsService.loadUserByEmail(signInDetails.getUserId());
			} else {
			    userDetails = (SlippUser)userDetailsService.loadUserByUsername(signInDetails.getUserId());
			}
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
					userDetails.getPassword(), userDetails.getAuthorities());
			authenticationToken.setDetails(userDetails.getProviderType());
			return authenticationToken;
		}
	}
}
