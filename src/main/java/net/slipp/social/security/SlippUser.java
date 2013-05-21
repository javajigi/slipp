package net.slipp.social.security;

import java.util.Collection;

import net.slipp.domain.ProviderType;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class SlippUser extends User {
    private static final long serialVersionUID = -6016106430103939789L;
    
    private ProviderType providerType;

    public SlippUser(String username, String password, ProviderType providerType, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.providerType = providerType;
    }
    
    public ProviderType getProviderType() {
        return providerType;
    }
}
