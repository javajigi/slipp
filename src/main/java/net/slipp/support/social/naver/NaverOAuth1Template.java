package net.slipp.support.social.naver;

import org.springframework.social.oauth1.AuthorizedRequestToken;
import org.springframework.social.oauth1.OAuth1Parameters;
import org.springframework.social.oauth1.OAuth1Template;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.util.MultiValueMap;

public class NaverOAuth1Template extends OAuth1Template {
    public NaverOAuth1Template(String consumerKey, String consumerSecret,
            String requestTokenUrl, String authorizeUrl, String accessTokenUrl) {
        super(consumerKey, consumerSecret, requestTokenUrl, authorizeUrl,
                null, accessTokenUrl);
    }

    @Override
    public OAuthToken fetchRequestToken(String callbackUrl,
            MultiValueMap<String, String> additionalParameters) {
        if (additionalParameters == null) {
            additionalParameters = new OAuth1Parameters();
        }
        additionalParameters.add("mode", "req_req_token");
        return super.fetchRequestToken(callbackUrl, additionalParameters);
    }
    
    @Override
    public String buildAuthorizeUrl(String requestToken,
            OAuth1Parameters parameters) {
        if (parameters == null) {
            parameters = new OAuth1Parameters();
        }
        parameters.add("mode", "auth_req_token");
        return super.buildAuthorizeUrl(requestToken, parameters);
    }
    
    @Override
    public OAuthToken exchangeForAccessToken(
            AuthorizedRequestToken requestToken,
            MultiValueMap<String, String> additionalParameters) {
        if (additionalParameters == null) {
            additionalParameters = new OAuth1Parameters();
        }
        additionalParameters.add("mode", "req_acc_token");
        return super.exchangeForAccessToken(requestToken, additionalParameters);
    }
}
