package net.slipp.support.social.naver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.oauth1.AuthorizedRequestToken;
import org.springframework.social.oauth1.OAuth1Operations;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationContext.xml")
public class TwitterConnectionFactoryTest {
    private static final Logger logger = LoggerFactory.getLogger(TwitterConnectionFactoryTest.class);
    
    @Value("#{applicationProperties['twitter.consumerKey']}")
    private String consumerKey;
    
    @Value("#{applicationProperties['twitter.consumerSecret']}")
    private String consumerSecret;
    
    @Test
    public void oauth() {
        TwitterConnectionFactory connectionFactory = new TwitterConnectionFactory(consumerKey, consumerSecret);
        OAuth1Operations operations = connectionFactory.getOAuthOperations();
        OAuthToken requestToken = operations.fetchRequestToken("http://localhost:8080", null);
        logger.debug("request token : {}", requestToken.getValue());
        String authorizeUrl = operations.buildAuthorizeUrl(requestToken.getValue(), null);
        logger.debug("authorizeUrl : {}", authorizeUrl);
        OAuthToken accessToken = operations.exchangeForAccessToken(new AuthorizedRequestToken(requestToken, "http://www.slipp.net"), null);
        logger.debug("access token : {}", accessToken.getValue());
    }
}
