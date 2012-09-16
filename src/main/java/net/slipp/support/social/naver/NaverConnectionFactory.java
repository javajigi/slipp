package net.slipp.support.social.naver;

import org.springframework.social.connect.support.OAuth1ConnectionFactory;

public class NaverConnectionFactory extends OAuth1ConnectionFactory<Naver> {
    public NaverConnectionFactory(String consumerKey, String consumerSecret) {
        super("naver", new NaverServiceProvider(consumerKey, consumerSecret), new NaverAdapter());
    }
}
