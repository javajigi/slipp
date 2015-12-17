package net.slipp.service.qna;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.FacebookType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import slipp.config.TestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class, loader = AnnotationConfigContextLoader.class)
public class NotificationTest {
    private static Logger logger = LoggerFactory.getLogger(NotificationTest.class);

    @Autowired
    private Environment env;

    private FacebookClient.AccessToken createAccessToken() {
        FacebookClient.AccessToken accessToken =
                new DefaultFacebookClient(Version.VERSION_2_2)
                        .obtainAppAccessToken(env.getProperty("facebook.clientId"), env.getProperty("facebook.clientSecret"));
        logger.debug("AccessToken : {}", accessToken);
        return accessToken;
    }

    @Test
    public void notification() {
        FacebookClient.AccessToken accessToken = createAccessToken();
        FacebookClient facebookClient = new DefaultFacebookClient(accessToken.getAccessToken(), Version.VERSION_2_0);
        String uri = String.format("/%s/notifications", "1324855987");
        String template = String.format("%s님이 \"%s\" 글에 답변을 달았습니다.", "javajigi", "this is title");
        String href = String.format("/questions/1#answer-5");

        FacebookType result = facebookClient.publish(uri, FacebookType.class, Parameter.with("template", template),
                Parameter.with("href", href));
        logger.debug("result : {}", result);
    }
}
