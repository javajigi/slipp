package net.slipp.social.security;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.ServletWebRequest;

@Controller
@RequestMapping("/signup")
public class SlippSecuritySignUpController {
	private String authenticateUrl = SlippSecurityAuthenticationFilter.DEFAULT_AUTHENTICATION_URL;

	@Resource(name = "usersConnectionRepository")
	private UsersConnectionRepository usersConnectionRepository;

	@Autowired
	private SignInAdapter signInAdapter;

	public void setAuthenticateUrl(String authenticateUrl) {
		this.authenticateUrl = authenticateUrl;
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String signUpForm(ServletWebRequest request, Model model) {
		Connection<?> connection = ProviderSignInUtils.getConnection(request);
		ConnectionData connectionData = connection.createData();
		SignUpForm signUpForm = new SignUpForm();
		signUpForm.setUsername(connectionData.getDisplayName());
		model.addAttribute("signUpForm", signUpForm);
		return "signUpForm";
	}

	private boolean isUserNameValid(String username, BindingResult errors) {
		if (StringUtils.isEmpty(username)) {
			errors.addError(new FieldError("signUpForm", "userName", "Please choose a username"));
			return false;
		} else {
			return true;
		}
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public String signUpSubmit(ServletWebRequest request, SignUpForm signUpForm, BindingResult result) {
		Connection<?> connection = ProviderSignInUtils.getConnection(request);
		if (isUserNameValid(signUpForm.getUsername(), result)) {
			ConnectionRepository connectionRepository = usersConnectionRepository.createConnectionRepository(signUpForm
					.getUsername());
			connectionRepository.addConnection(connection);
			signInAdapter.signIn(signUpForm.getUsername(), connection, request);
			return "redirect:" + authenticateUrl;
		} else {
			return "signUpForm";
		}
	}
}
