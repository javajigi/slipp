package net.slipp.support.security;

import java.util.ArrayList;
import java.util.List;

import net.slipp.domain.user.SocialUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public abstract class MockUserDetailsService implements UserDetailsService {
    private static Logger log = LoggerFactory.getLogger(MockUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SocialUser socialUser = createSocialUser();
        return new User(username, socialUser.getPassword(), true, true, true, true, createGrantedAuthorities(username));  
    }
    
    protected abstract SocialUser createSocialUser();
    
    private List<GrantedAuthority> createGrantedAuthorities(String username) {
        log.debug("UserName : {}", username);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return grantedAuthorities;
    }
}
