package net.slipp.service.qna;

import java.util.List;

import net.slipp.domain.qna.FacebookComment;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.Comment;
import com.restfb.types.FacebookType;
import com.restfb.types.Post;
import com.restfb.types.Post.Comments;

public class RestFBTest {
    private static Logger logger = LoggerFactory.getLogger(RestFBTest.class);

    private FacebookClient dut;

    @Before
    public void setup() {
        String accessToken = "AAACm7YIxcxcBAK5umCPnYRC90q3POoFxKeyOTqQoXJdPDOm0X3gGSH4ZC5ZCuNSScn9pjwCfKT4pS1yrt4ZClp2I7bq3bjEd5PkNaVTHgZDZD";
        dut = new DefaultFacebookClient(accessToken);
    }

    @Test
    public void post() throws Exception {
        String message = "그룹에 글쓰기 테스트입니다.";
        FacebookType response = dut.publish("me/feed", FacebookType.class,
                Parameter.with("link", "http://www.slipp.net/questions/87"), Parameter.with("message", message));
        String id = response.getId();
        logger.debug("id : {}", id);
    }

    @Test
    public void groupPost() throws Exception {
        String message = "그룹에 글쓰기 테스트입니다.";
        FacebookType response = dut.publish("387530111326987/feed", FacebookType.class,
                Parameter.with("link", "http://www.slipp.net/questions/87"), Parameter.with("message", message));
        String id = response.getId();
        logger.debug("id : {}", id);
    }

    @Test
    public void fetchPost() throws Exception {
        Post post = dut.fetchObject("1324855987_390710267709840", Post.class);
        logger.debug("Post: " + post.getId() + " : " + post.getMessage());
        Comments comments = post.getComments();
        List<Comment> commentData = comments.getData();
        for (Comment comment : commentData) {
            FacebookComment fbComment = FacebookComment.create(comment);
            logger.debug("fbComment: " + fbComment);
        }
    }
}
