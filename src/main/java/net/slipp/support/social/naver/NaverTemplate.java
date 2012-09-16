package net.slipp.support.social.naver;

import org.springframework.social.oauth1.AbstractOAuth1ApiBinding;
import org.springframework.social.twitter.api.UserOperations;

public class NaverTemplate extends AbstractOAuth1ApiBinding implements Naver {

	public NaverTemplate(String consumerKey, String consumerSecret, String accessToken, String secret) {
	    super(consumerKey, consumerSecret, accessToken, secret);
	}

	@Override
	public UserOperations userOperations() {
		// TODO Auto-generated method stub
		return null;
	}

}
