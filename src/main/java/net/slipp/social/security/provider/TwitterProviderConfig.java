package net.slipp.social.security.provider;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.web.ConnectInterceptor;
import org.springframework.social.twitter.api.Twitter;

@Configuration
public class TwitterProviderConfig extends AbstractProviderConfig<Twitter> {
	
	@Autowired
	private TwitterConnectInterceptor twitterConnectInterceptor;

	@Override
	protected ConnectInterceptor<Twitter> getConnectInterceptor() {
		return twitterConnectInterceptor;
	}
}
