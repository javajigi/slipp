package net.slipp.service.qna;

import javax.annotation.Resource;

import net.slipp.domain.qna.Answer;
import net.slipp.domain.qna.Question;
import net.slipp.domain.user.SocialUser;
import net.slipp.repository.qna.AnswerRepository;
import net.slipp.repository.qna.QuestionRepository;
import net.slipp.support.web.tags.SlippFunctions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;

@Service
@Transactional
public class FacebookService {
	private static final Logger log = LoggerFactory.getLogger(FacebookService.class);
	
	private static final int DEFAULT_FACEBOOK_MESSAGE_LENGTH = 250;
	
	@Resource(name = "questionRepository")
	private QuestionRepository questionRepository;
	
	@Resource(name = "answerRepository")
	private AnswerRepository answerRepository;
	
	@Value("${application.url}")
	private String applicationUrl;
	
	@Async
	public void sendToQuestionMessage(SocialUser loginUser, Long questionId) {
	    log.info("questionId : {}", questionId);
		Question question = questionRepository.findOne(questionId);
		log.info("question : {}", question);
		String message = createFacebookMessage(question.getContents());
		log.info("message : {}", message);
		
		String postId = sendMessageToFacebook(loginUser.getAccessToken(), createLink(question.getQuestionId()), message);
		question.connected(postId);
	}

	private String sendMessageToFacebook(String accessToken, String link, String message) {
		FacebookClient facebookClient = new DefaultFacebookClient(accessToken);
		FacebookType response = facebookClient.publish("me/feed", FacebookType.class, 
		    Parameter.with("link", link),
			Parameter.with("message", message));
		String postId = response.getId();
		log.info("connect post id : {}", postId);
		return postId;
	}
	
	@Async
	public void sendToAnswerMessage(SocialUser loginUser, Long answerId) {
	    log.info("answerId : {}", answerId);
		Answer answer = answerRepository.findOne(answerId);
		log.info("answer : {}", answer);
		Question question = answer.getQuestion();
		String message = createFacebookMessage(answer.getContents());
		log.info("message : {}", message);
		
		String postId = sendMessageToFacebook(loginUser.getAccessToken(), createLink(question.getQuestionId()), message);
		answer.connected(postId);
	}

	private String createLink(Long questionId) {
	    String link = String.format("%s/questions/%d", applicationUrl, questionId);
	    log.info("create link : {}", link);
        return link;
    }

    private String createFacebookMessage(String contents) {
		String wikiContents = SlippFunctions.wiki(contents);
		return SlippFunctions.stripTagsAndCut(wikiContents, DEFAULT_FACEBOOK_MESSAGE_LENGTH, "...");
	}
}
