package slipp.config;

import com.google.common.collect.Lists;
import net.slipp.repository.user.SocialUserRepository;
import net.slipp.repository.user.SocialUsersConnectionRepository;
import net.slipp.social.security.SlippSecuritySignInAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

import java.util.List;

@Configuration
@ComponentScan("net.slipp.support.security")
public class SpringSocialConfig {
    @Autowired
    private Environment env = null;

    @Autowired
    private SocialUserRepository socialUserRepository = null;

    @Bean
    public ConnectionFactory facebookConnectionFactory() {
        return new FacebookConnectionFactory(env.getProperty("facebook.clientId"), env.getProperty("facebook.clientSecret"));
    }

    @Bean
    public ConnectionFactory twitterConnectionFactory() {
        return new TwitterConnectionFactory(env.getProperty("twitter.consumerKey"), env.getProperty("twitter.consumerSecret"));
    }

    @Bean
    public ConnectionFactory googleConnectionFactory() {
        return new GoogleConnectionFactory(env.getProperty("google.clientId"), env.getProperty("google.clientSecret"));
    }

    @Bean
    public List<ConnectionFactory<?>> connectionFactories() {
        return Lists.newArrayList(facebookConnectionFactory(), twitterConnectionFactory(), googleConnectionFactory());
    }

    @Bean
    public ConnectionFactoryRegistry connectionFactoryRegistry() {
        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
        registry.setConnectionFactories(connectionFactories());
        return registry;
    }

    @Bean
    public TextEncryptor textEncryptor() {
        return Encryptors.noOpText();
    }

    @Bean
    public SocialUsersConnectionRepository usersConnectionRepository() {
        return new SocialUsersConnectionRepository(socialUserRepository, connectionFactoryRegistry(), textEncryptor());
    }

    @Bean
    public SlippSecuritySignInAdapter signInAdapter() {
        return new SlippSecuritySignInAdapter();
    }

    @Bean
    public ProviderSignInUtils providerSignInUtils() {
        return new ProviderSignInUtils(connectionFactoryRegistry(), usersConnectionRepository());
    }
}
