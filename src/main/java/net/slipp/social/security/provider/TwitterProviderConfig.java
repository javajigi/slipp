package net.slipp.social.security.provider;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.web.ConnectInterceptor;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

@Configuration
public class TwitterProviderConfig extends AbstractProviderConfig<Twitter> {
	
	@Autowired
	private TwitterConnectInterceptor twitterConnectInterceptor;

	
	@Value("${twitter.consumerKey}")
	private String twitterConsumerKey;

	@Value("${twitter.consumerSecret}")
	private String twitterConsumerSecret;

	@Override
	protected ConnectionFactory<Twitter> createConnectionFactory() {
		return new TwitterConnectionFactory(
				twitterConsumerKey, twitterConsumerSecret);
	}

	@Override
	protected ConnectInterceptor<Twitter> getConnectInterceptor() {
		return twitterConnectInterceptor;
	}
	
	

}
