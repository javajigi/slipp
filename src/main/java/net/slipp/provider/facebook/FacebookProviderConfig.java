package net.slipp.provider.facebook;

import net.slipp.provider.AbstractProviderConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.web.ConnectInterceptor;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

@Configuration
public class FacebookProviderConfig extends AbstractProviderConfig<Facebook> {
	
	
	@Autowired
	private FacebookConnectInterceptor facebookConnectInterceptor;
	
	@Value("${facebook.clientId}")
	private String facebookClientId;

	@Value("${facebook.clientSecret}")
	private String facebookClientSecret;

	@Override
	protected ConnectionFactory<Facebook> createConnectionFactory() {
		return new FacebookConnectionFactory(
				facebookClientId, facebookClientSecret);
	}

	@Override
	protected ConnectInterceptor<Facebook> getConnectInterceptor() {
		return facebookConnectInterceptor;
	}
	

}
