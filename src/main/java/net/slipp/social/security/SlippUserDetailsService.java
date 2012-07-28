package net.slipp.social.security;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.slipp.domain.user.SocialUser;
import net.slipp.repository.user.SocialUserRepository;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.util.MultiValueMap;

public class SlippUserDetailsService implements UserDetailsService {
	@Resource(name = "socialUserRepository")
	private SocialUserRepository socialUserRepository;

	@Resource(name = "usersConnectionRepository")
	private UsersConnectionRepository usersConnectionRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ConnectionRepository connectionRepository = usersConnectionRepository.createConnectionRepository(username);
		List<Connection<?>> allConnections = getConnections(connectionRepository);
		if (allConnections.size() > 0) {
			Connection<?> connection = allConnections.get(0);
			ConnectionKey connectionKey = connection.getKey();
			SocialUser socialUser = socialUserRepository.findByUserIdAndProviderIdAndProviderUserId(username,
					connectionKey.getProviderId(), connectionKey.getProviderUserId());
			return new User(username, socialUser.getAccessToken(), true, true, true, true, createGrantedAuthorities());
		} else {
			throw new UsernameNotFoundException(String.format("%s not found!", username));
		}
	}

	private List<GrantedAuthority> createGrantedAuthorities() {
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		return grantedAuthorities;
	}

	private List<Connection<?>> getConnections(ConnectionRepository connectionRepository) {
		MultiValueMap<String, Connection<?>> connections = connectionRepository.findAllConnections();
		List<Connection<?>> allConnections = new ArrayList<Connection<?>>();
		if (connections.size() > 0) {
			for (List<Connection<?>> connectionList : connections.values()) {
				for (Connection<?> connection : connectionList) {
					allConnections.add(connection);
				}
			}
		}
		return allConnections;
	}
}
