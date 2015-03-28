package net.slipp.social.security;

import javax.inject.Inject;

import org.apache.http.annotation.NotThreadSafe;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.SignInAdapter;

@NotThreadSafe
public class FixedProviderSignInController extends ProviderSignInController {
	private String applicationUrl;
	private boolean isInit = false;

	@Inject
	public FixedProviderSignInController(ConnectionFactoryLocator connectionFactoryLocator, UsersConnectionRepository usersConnectionRepository, SignInAdapter signInAdapter) {
        super(connectionFactoryLocator, usersConnectionRepository, signInAdapter);
    }
	
	@Override
    public void setApplicationUrl(String applicationUrl) {
        if(isInit) {
            super.setApplicationUrl(applicationUrl);
            return;
        }
        this.applicationUrl = applicationUrl;
    }
 
    // NOT ThreadSafe
    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        if(applicationUrl != null) {
            super.setApplicationUrl(applicationUrl);
        }
        isInit = true;
    }
}
