package net.slipp.support.social.naver;

import org.springframework.social.oauth1.AbstractOAuth1ServiceProvider;

public class NaverServiceProvider extends AbstractOAuth1ServiceProvider<Naver> {
	public NaverServiceProvider(String consumerKey, String consumerSecret) {
		super(consumerKey, consumerSecret, new NaverOAuth1Template(consumerKey, consumerSecret,
			"https://nid.naver.com/naver.oauth",
			"https://nid.naver.com/naver.oauth",
			"https://nid.naver.com/naver.oauth"));
	}

	public Naver getApi(String accessToken, String secret) {
		return new NaverTemplate(getConsumerKey(), getConsumerSecret(), accessToken, secret);
	}
}
