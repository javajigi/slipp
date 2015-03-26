package net.slipp.service.qna;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import net.slipp.domain.fb.FacebookComment;
import net.slipp.domain.qna.Answer;
import net.slipp.domain.qna.Question;
import net.slipp.domain.qna.SnsConnection;
import net.slipp.domain.tag.Tag;
import net.slipp.domain.user.SocialUser;
import net.slipp.repository.qna.AnswerRepository;
import net.slipp.repository.qna.QuestionRepository;
import net.slipp.repository.tag.TagRepository;
import net.slipp.support.web.tags.SlippFunctions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.exception.FacebookGraphException;
import com.restfb.types.Comment;
import com.restfb.types.FacebookType;
import com.restfb.types.Group;
import com.restfb.types.Post;
import com.restfb.types.Post.Comments;

@Service
@Transactional
public class FacebookService {
	private static final Logger log = LoggerFactory.getLogger(FacebookService.class);

	private static final int DEFAULT_FACEBOOK_MESSAGE_LENGTH = 250;

	@Resource(name = "questionRepository")
	private QuestionRepository questionRepository;

	@Resource(name = "answerRepository")
	private AnswerRepository answerRepository;

	@Resource(name = "tagRepository")
	private TagRepository tagRepository;

	@Value("${facebook.application.url}")
	private String applicationUrl;

	@Async
	public void sendToQuestionMessage(SocialUser loginUser, Long questionId) {
		log.info("questionId : {}", questionId);
		Question question = questionRepository.findOne(questionId);
		Assert.notNull(question, "Question should be not null!");

		String message = createFacebookMessage(question.getContents());
		String postId = sendMessageToFacebook(loginUser, createLink(question.getQuestionId()), "me", message);
		if (postId != null) {
			question.connected(postId);
		}
	}

	private String sendMessageToFacebook(SocialUser loginUser, String link, String receiverId, String message) {
		String postId = null;
		try {
			FacebookClient facebookClient = createFacebookClient(loginUser);
			int i = 0;
			do {
				if (i > 2) {
					break;
				}

				FacebookType response = facebookClient.publish(receiverId + "/feed", FacebookType.class,
						Parameter.with("link", link), Parameter.with("message", message));
				postId = response.getId();

				i++;
			} while (postId == null);
			log.debug("connect post id : {}", postId);
		} catch (Throwable e) {
			log.error("Facebook Connection Failed : {}", e.getMessage());
		}
		return postId;
	}

	private FacebookClient createFacebookClient(SocialUser socialUser) {
		return new DefaultFacebookClient(socialUser.getAccessToken(), Version.VERSION_2_2);
	}

	@Async
	public void sendToGroupQuestionMessage(SocialUser loginUser, Long questionId) {
		log.info("questionId : {}", questionId);
		Question question = questionRepository.findOne(questionId);
		Assert.notNull(question, "Question should be not null!");

		Set<Tag> connectedGroupTags = question.getConnectedGroupTag();
		if (connectedGroupTags.isEmpty()) {
			return;
		}

		String message = createFacebookMessage(question.getContents());
		String link = createLink(question.getQuestionId());
		for (Tag connectedGroupTag : connectedGroupTags) {
			String postId = sendMessageToFacebook(loginUser, link, connectedGroupTag.getTagInfo().getGroupId(), message);
			if (postId != null) {
				question.connected(postId, connectedGroupTag.getGroupId());
			}
		}
	}

	@Async
	public void sendToAnswerMessage(SocialUser loginUser, Long answerId) {
		log.info("answerId : {}", answerId);
		Answer answer = answerRepository.findOne(answerId);
		Assert.notNull(answer, "Answer should be not null!");

		Question question = answer.getQuestion();
		String message = createFacebookMessage(answer.getContents());

		String postId = sendMessageToFacebook(loginUser, createLink(question.getQuestionId(), answerId), "me", message);
		if (postId != null) {
			answer.connected(postId);
		}
	}

	public List<FacebookComment> findFacebookComments(Long questionId) {
		Question question = questionRepository.findOne(questionId);
		if (!question.isSnsConnected()) {
			return new ArrayList<FacebookComment>();
		}

		SocialUser socialUser = question.getWriter();
		FacebookClient facebookClient = createFacebookClient(socialUser);
		Collection<SnsConnection> snsConnections = question.getSnsConnection();
		List<FacebookComment> fbComments = Lists.newArrayList();
		for (SnsConnection snsConnection : snsConnections) {
			log.debug("postId : {}", snsConnection.getPostId());
			Post post = findPost(facebookClient, snsConnection.getPostId());
			
			List<FacebookComment> fbCommentsPerConnection = null;
			if (snsConnection.isGroupConnected()) {
				Tag tag = tagRepository.findByGroupId(snsConnection.getGroupId());
				fbCommentsPerConnection = findComments(post, tag);
			} else {
				fbCommentsPerConnection = findComments(post, null);
			}
			fbComments.addAll(fbCommentsPerConnection);
			log.debug("count comments : {}, from post : {}", fbCommentsPerConnection.size(), snsConnection.getPostId());
			snsConnection.updateAnswerCount(fbCommentsPerConnection.size());
		}
		Collections.sort(fbComments);
		return fbComments;
	}

	@Cacheable(value="fbgroups", key="#loginUser.id")
	public List<Group> findFacebookGroups(SocialUser loginUser) {
		int groupLimit = 10;
		FacebookClient facebookClient = createFacebookClient(loginUser);
		Connection<Group> myGroups = facebookClient.fetchConnection("/me/groups", Group.class,
				Parameter.with("limit", groupLimit));
		List<Group> allGroups = Lists.newArrayList();
		for (List<Group> groups : myGroups) {
			allGroups.addAll(groups);
		}
		
		if (allGroups.size() < groupLimit) {
			groupLimit = allGroups.size();
		}
		
		return allGroups.subList(0, groupLimit);
	}

	private Post findPost(FacebookClient facebookClient, String postId) {
		try {
			return facebookClient.fetchObject(postId, Post.class);
		} catch (FacebookGraphException e) {
			log.error("{} postId, errorMessage : {}", postId, e.getMessage());
			return null;
		}
	}

	private List<FacebookComment> findComments(Post post, Tag tag) {
		List<FacebookComment> fbComments = Lists.newArrayList();
		if (post == null) {
			return fbComments;
		}

		Comments comments = post.getComments();
		if (comments == null) {
			return fbComments;
		}

		List<Comment> commentData = comments.getData();
		for (Comment comment : commentData) {
			fbComments.add(FacebookComment.create(tag, comment));
		}
		return fbComments;
	}

	String createLink(Long questionId) {
		String link = String.format("%s/questions/%d", createApplicationUrl(), questionId);
		return link;
	}

	String createLink(Long questionId, Long answerId) {
		String link = String.format("%s/questions/%d#answer-%d", createApplicationUrl(), questionId, answerId);
		return link;
	}

	protected String createApplicationUrl() {
		return applicationUrl;
	}

	private String createFacebookMessage(String contents) {
		String wikiContents = SlippFunctions.wiki(contents);
		return SlippFunctions.stripTagsAndCut(wikiContents, DEFAULT_FACEBOOK_MESSAGE_LENGTH, "...");
	}
}
