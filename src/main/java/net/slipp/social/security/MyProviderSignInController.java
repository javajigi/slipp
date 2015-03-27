package net.slipp.social.security;

import javax.inject.Inject;

import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signin")
public class MyProviderSignInController extends ProviderSignInController {
	@Inject
	public MyProviderSignInController(ConnectionFactoryLocator connectionFactoryLocator,
			UsersConnectionRepository usersConnectionRepository, SignInAdapter signInAdapter) throws Exception {
		super(connectionFactoryLocator, usersConnectionRepository, signInAdapter);
		super.afterPropertiesSet();
	}
}
