package net.slipp.service.qna;

import java.util.List;

import net.slipp.domain.fb.FacebookComment;
import net.slipp.domain.fb.FacebookGroup;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.Comment;
import com.restfb.types.FacebookType;
import com.restfb.types.Group;
import com.restfb.types.Post;
import com.restfb.types.Post.Comments;

@Ignore
public class RestFBTest {
	private static Logger logger = LoggerFactory.getLogger(RestFBTest.class);

	private FacebookClient dut;

	@Before
	public void setup() {
		String accessToken = "ACCESS_TOKEN";
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
			FacebookComment fbComment = FacebookComment.create(null, comment);
			logger.debug("fbComment: " + fbComment);
		}
	}

	@Test
	public void findGroups() throws Exception {
		Connection<Group> myGroups = dut.fetchConnection("/me/groups", Group.class,
				Parameter.with("limit", 10));
		logger.debug("group size : {}", myGroups.getData());
		for (List<Group> groups : myGroups) {
			for (Group group : groups) {
				logger.debug("group name : {}", group.getName());
			}
		}
	}
	
	@Test
	public void findGroupsByFql() throws Exception {
		String query = "SELECT gid, name FROM group WHERE gid IN " + 
				"(SELECT gid FROM group_member WHERE uid = me() AND bookmark_order <= 10 order by bookmark_order ASC)";
		List<FacebookGroup> myGroups = dut.executeFqlQuery(query, FacebookGroup.class);
		logger.debug("group size : {}", myGroups);
	}
}
