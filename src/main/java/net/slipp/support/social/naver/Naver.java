package net.slipp.support.social.naver;

import org.springframework.social.ApiBinding;
import org.springframework.social.twitter.api.UserOperations;

public interface Naver extends ApiBinding {
	UserOperations userOperations();
}
