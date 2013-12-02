package net.slipp.service.rank;

import net.slipp.domain.qna.ScoreLike;
import net.slipp.domain.qna.ScoreLikeType;
import net.slipp.repository.qna.ScoreLikeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 향후 질문(Quest)와의 동적 처리
 * 
 * @author eclipse4j
 * 
 */
@Service
public class ScoreLikeService {

    @Autowired
    private ScoreLikeRepository scoreLikeRepository;
    
    public boolean alreadyLikedQuestion(Long questionId, Long socialUserId) {
        ScoreLike scoreLike = scoreLikeRepository.findBySocialUserIdAndLike(ScoreLikeType.QUESTION, questionId,
                socialUserId);
        if (scoreLike == null) {
            return false;
        }
        return true;
    }    

    public boolean alreadyLikedAnswer(Long answerId, Long socialUserId) {
        ScoreLike scoreLike = scoreLikeRepository.findBySocialUserIdAndLike(ScoreLikeType.ANSWER, answerId,
                socialUserId);
        if (scoreLike == null) {
            return false;
        }
        return true;
    }
    
    public boolean alreadyDisLikedAnswer(Long answerId, Long socialUserId) {
        ScoreLike scoreLike = scoreLikeRepository.findBySocialUserIdAndDisLike(ScoreLikeType.ANSWER, answerId,
                socialUserId);
        if (scoreLike == null) {
            return false;
        }
        return true;
    }

    public void saveLikeAnswer(Long answerId, Long socialUserId) {
        scoreLikeRepository.save(ScoreLike.createLikedScoreLike(ScoreLikeType.ANSWER, socialUserId, answerId));
    }
    
    public void saveDisLikeAnswer(Long answerId, Long socialUserId) {
        scoreLikeRepository.save(ScoreLike.createDisLikedScoreLike(ScoreLikeType.ANSWER, socialUserId, answerId));
    }

    public void saveLikeQuestion(Long questionId, Long socialUserId) {
        scoreLikeRepository.save(ScoreLike.createLikedScoreLike(ScoreLikeType.QUESTION, socialUserId, questionId));
    }
}
