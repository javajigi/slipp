package net.slipp.support.social.naver;

import org.springframework.social.oauth1.AbstractOAuth1ServiceProvider;
import org.springframework.social.oauth1.OAuth1Template;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

public class NaverServiceProvider extends AbstractOAuth1ServiceProvider<Naver> {
	public NaverServiceProvider(String consumerKey, String consumerSecret) {
		super(consumerKey, consumerSecret, new OAuth1Template(consumerKey, consumerSecret,
			"https://nid.naver.com/naver.oauth?mode=req_req_token",
			"https://nid.naver.com/naver.oauth?mode=auth_req_token",
			null,			
			"https://nid.naver.com/naver.oauth?mode=req_acc_token"));
	}

	public Naver getApi(String accessToken, String secret) {
		return new NaverTemplate(getConsumerKey(), getConsumerSecret(), accessToken, secret);
	}
}
