package net.slipp.social.security;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signin")
public class MyProviderSignInController extends ProviderSignInController {
	private static final Logger log = LoggerFactory.getLogger(MyProviderSignInController.class);
	
	@Inject
	public MyProviderSignInController(ConnectionFactoryLocator connectionFactoryLocator,
			UsersConnectionRepository usersConnectionRepository, SignInAdapter signInAdapter) throws Exception {
		super(connectionFactoryLocator, usersConnectionRepository, signInAdapter);
		super.afterPropertiesSet();
		log.debug("call parent");
	}
}
