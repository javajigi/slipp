package net.slipp.social.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import net.slipp.domain.user.SocialUser;
import net.slipp.service.user.SocialUserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class SlippUserDetailsService implements UserDetailsService {
	private static Logger logger = LoggerFactory.getLogger(SlippUserDetailsService.class);
	
	@Resource(name = "socialUserService")
	private SocialUserService socialUserService;

	private String adminUsers;
	
	public void setAdminUsers(String adminUsers) {
		this.adminUsers = adminUsers;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SocialUser socialUser = socialUserService.findByUserId(username);
		if (socialUser == null) {
			throw new UsernameNotFoundException(String.format("%s not found!", username));
		} else {
			return new User(username, socialUser.getAccessToken(), true, true, true, true, createGrantedAuthorities(username));
		}
	}

	private List<GrantedAuthority> createGrantedAuthorities(String username) {
		logger.debug("UserName : {}", username);
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		if (isAdmin(username)){
			grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMINISTRATOR"));
		}
		return grantedAuthorities;
	}

	boolean isAdmin(String username) {
		String[] userIds = adminUsers.split(":");
		return Arrays.asList(userIds).contains(username);
	}
}