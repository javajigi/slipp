package net.slipp.service.qna;

import javax.annotation.Resource;

import net.slipp.domain.qna.Question;
import net.slipp.domain.user.SocialUser;
import net.slipp.repository.qna.QuestionRepository;
import net.slipp.support.web.tags.SlippFunctions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;

@Service
@Transactional
public class FacebookService {
	private static final Logger logger = LoggerFactory.getLogger(FacebookService.class);
	
	private static final int DEFAULT_FACEBOOK_MESSAGE_LENGTH = 250;
	
	@Resource(name = "questionRepository")
	private QuestionRepository questionRepository;
	
	public void sendToMessage(SocialUser loginUser, Long questionId) {
		Question question = questionRepository.findOne(questionId);
		String message = createFacebookMessage(question.getContents());
		
		FacebookClient facebookClient = new DefaultFacebookClient(loginUser.getAccessToken());
		FacebookType response = facebookClient.publish("me/feed", FacebookType.class, 
			Parameter.with("message", message));
		String postId = response.getId();
		logger.debug("connect post id : {}", postId);
		question.connected(postId);
	}

	private String createFacebookMessage(String contents) {
		String wikiContents = SlippFunctions.wiki(contents);
		return SlippFunctions.stripTagsAndCut(wikiContents, DEFAULT_FACEBOOK_MESSAGE_LENGTH, "...");
	}
}
