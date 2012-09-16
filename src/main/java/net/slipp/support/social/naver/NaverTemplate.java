package net.slipp.support.social.naver;

import org.springframework.social.twitter.api.UserOperations;

public class NaverTemplate implements Naver {

	public NaverTemplate(String consumerKey, String consumerSecret, String accessToken, String secret) {
		// TwitterTemplate 참조해서 구현한다.
	}

	@Override
	public boolean isAuthorized() {
		return false;
	}

	@Override
	public UserOperations userOperations() {
		// TODO Auto-generated method stub
		return null;
	}

}
