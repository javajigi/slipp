package net.slipp.social.security;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.slipp.domain.user.SocialUser;
import net.slipp.domain.user.SocialUserService;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class SlippUserDetailsService implements UserDetailsService {
	@Resource(name = "socialUserService")
	private SocialUserService socialUserService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SocialUser socialUser = socialUserService.findByUserId(username);
		if (socialUser == null) {
			throw new UsernameNotFoundException(String.format("%s not found!", username));
		} else {
			return new User(username, socialUser.getAccessToken(), true, true, true, true, createGrantedAuthorities());
		}
	}

	private List<GrantedAuthority> createGrantedAuthorities() {
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		return grantedAuthorities;
	}
}