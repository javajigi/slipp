package net.slipp.social.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.slipp.domain.ProviderType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.util.StringUtils;

public class SlippTokenBasedRememberMeServices extends AbstractRememberMeServices {
    private static Logger log = LoggerFactory.getLogger(SlippTokenBasedRememberMeServices.class);
    
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @SuppressWarnings("deprecation")
    public SlippTokenBasedRememberMeServices() {
    }
    
    public SlippTokenBasedRememberMeServices(String key, UserDetailsService userDetailsService) {
        super(key, userDetailsService);
    }

    @Override
    protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request,
            HttpServletResponse response) {
        if (cookieTokens.length != 4) {
            throw new InvalidCookieException("Cookie token did not contain 4" +
                    " tokens, but contained '" + Arrays.asList(cookieTokens) + "'");
        }
        
        log.debug("cookieTokens userId : {}", cookieTokens[0]);

        long tokenExpiryTime;

        try {
            tokenExpiryTime = new Long(cookieTokens[1]).longValue();
        }
        catch (NumberFormatException nfe) {
            throw new InvalidCookieException("Cookie token[1] did not contain a valid number (contained '" +
                    cookieTokens[1] + "')");
        }
        
        if (isTokenExpired(tokenExpiryTime)) {
            throw new InvalidCookieException("Cookie token[1] has expired (expired on '"
                    + new Date(tokenExpiryTime) + "'; current time is '" + new Date() + "')");
        }

        ProviderType providerType = ProviderType.valueOf(cookieTokens[3]);
        SlippUser userDetails = getSlippUserDetails(providerType, cookieTokens[0]);

        // Check signature of token matches remaining details.
        // Must do this after user lookup, as we need the DAO-derived password.
        // If efficiency was a major issue, just add in a UserCache implementation,
        // but recall that this method is usually only called once per HttpSession - if the token is valid,
        // it will cause SecurityContextHolder population, whilst if invalid, will cause the cookie to be cancelled.
        String expectedTokenSignature = makeTokenSignature(tokenExpiryTime, userDetails.getUsername(), userDetails.getPassword());
        if (!equals(expectedTokenSignature,cookieTokens[2])) {
            throw new InvalidCookieException("Cookie token[2] contained signature '" + cookieTokens[2]
                                                                                                    + "' but expected '" + expectedTokenSignature + "'");
        }
        
        return userDetails;
    }
    
    protected Authentication createSuccessfulAuthentication(HttpServletRequest request, UserDetails user) {
        RememberMeAuthenticationToken auth = new RememberMeAuthenticationToken(getKey(), user,
                authoritiesMapper.mapAuthorities(user.getAuthorities()));
        SlippUser slippUser = (SlippUser)user;
        auth.setDetails(slippUser.getProviderType());
        return auth;
    }

	private SlippUser getSlippUserDetails(ProviderType providerType, String firstCookieToken) {
        if (providerType == ProviderType.slipp) {
        	SlippUserDetailsService slippUserDetailsService = (SlippUserDetailsService)getUserDetailsService();
        	return (SlippUser)slippUserDetailsService.loadUserByEmail(firstCookieToken);
        } else {
        	return (SlippUser)getUserDetailsService().loadUserByUsername(firstCookieToken);
        }
	}
    
    /**
     * Calculates the digital signature to be put in the cookie. Default value is
     * MD5 ("username:tokenExpiryTime:password:key")
     */
    protected String makeTokenSignature(long tokenExpiryTime, String username, String password) {
        String data = username + ":" + tokenExpiryTime + ":" + password + ":" + getKey();
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No MD5 algorithm available!");
        }

        return new String(Hex.encode(digest.digest(data.getBytes())));
    }

    protected boolean isTokenExpired(long tokenExpiryTime) {
        return tokenExpiryTime < System.currentTimeMillis();
    }

    @Override
    public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication successfulAuthentication) {
        String username = retrieveUserName(successfulAuthentication);
        String password = retrievePassword(successfulAuthentication);
        ProviderType providerType = retrieveProviderType(successfulAuthentication);

        // If unable to find a username and password, just abort as TokenBasedRememberMeServices is
        // unable to construct a valid token in this case.
        if (!StringUtils.hasLength(username)) {
            logger.debug("Unable to retrieve username");
            return;
        }

        
        if (!StringUtils.hasLength(password)) {
        	SlippUser user = getSlippUserDetails(providerType, username);
            password = user.getPassword();

            if (!StringUtils.hasLength(password)) {
                logger.debug("Unable to obtain password for user: " + username);
                return;
            }
        }

        int tokenLifetime = calculateLoginLifetime(request, successfulAuthentication);
        long expiryTime = System.currentTimeMillis();
        // SEC-949
        expiryTime += 1000L* (tokenLifetime < 0 ? TWO_WEEKS_S : tokenLifetime);

        String signatureValue = makeTokenSignature(expiryTime, username, password);
        
        setCookie(new String[] {username, Long.toString(expiryTime), signatureValue, providerType.name()}, tokenLifetime, request, response);

        if (logger.isDebugEnabled()) {
            logger.info("Added remember-me cookie for user '" + username + "', expiry: '"
                    + new Date(expiryTime) + "'");
        }
    }

    /**
     * Calculates the validity period in seconds for a newly generated remember-me login.
     * After this period (from the current time) the remember-me login will be considered expired.
     * This method allows customization based on request parameters supplied with the login or information in
     * the <tt>Authentication</tt> object. The default value is just the token validity period property,
     * <tt>tokenValiditySeconds</tt>.
     * <p>
     * The returned value will be used to work out the expiry time of the token and will also be
     * used to set the <tt>maxAge</tt> property of the cookie.
     *
     * See SEC-485.
     *
     * @param request the request passed to onLoginSuccess
     * @param authentication the successful authentication object.
     * @return the lifetime in seconds.
     */
    protected int calculateLoginLifetime(HttpServletRequest request, Authentication authentication) {
        return getTokenValiditySeconds();
    }

    protected String retrieveUserName(Authentication authentication) {
        if (isInstanceOfUserDetails(authentication)) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        else {
            return authentication.getPrincipal().toString();
        }
    }

    protected String retrievePassword(Authentication authentication) {
        if (isInstanceOfUserDetails(authentication)) {
            return ((UserDetails) authentication.getPrincipal()).getPassword();
        }
        else {
            if (authentication.getCredentials() == null) {
                return null;
            }
            return authentication.getCredentials().toString();
        }
    }
    
    protected ProviderType retrieveProviderType(Authentication authentication) {
        return (ProviderType)authentication.getDetails();
    }

    private boolean isInstanceOfUserDetails(Authentication authentication) {
        return authentication.getPrincipal() instanceof UserDetails;
    }

    /**
     * Constant time comparison to prevent against timing attacks.
     */
    private static boolean equals(String expected, String actual) {
        byte[] expectedBytes = bytesUtf8(expected);
        byte[] actualBytes = bytesUtf8(actual);
        if (expectedBytes.length != actualBytes.length) {
            return false;
        }

        int result = 0;
        for (int i = 0; i < expectedBytes.length; i++) {
            result |= expectedBytes[i] ^ actualBytes[i];
        }
        return result == 0;
    }

    private static byte[] bytesUtf8(String s) {
        if (s == null) {
            return null;
        }
        return Utf8.encode(s);
    }
}