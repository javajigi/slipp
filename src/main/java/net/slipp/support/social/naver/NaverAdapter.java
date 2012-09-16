package net.slipp.support.social.naver;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

public class NaverAdapter implements ApiAdapter<Naver> {
	// TwitterAdapter 참조해서 구현한다.
	
	@Override
	public boolean test(Naver api) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setConnectionValues(Naver api, ConnectionValues values) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public UserProfile fetchUserProfile(Naver api) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateStatus(Naver api, String message) {
		// TODO Auto-generated method stub
		
	}

}
