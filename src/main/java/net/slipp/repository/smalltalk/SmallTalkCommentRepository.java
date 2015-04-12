package net.slipp.repository.smalltalk;

import java.util.List;

import net.slipp.domain.smalltalk.SmallTalk;
import net.slipp.domain.smalltalk.SmallTalkComment;
import net.slipp.domain.user.SocialUser;

import org.springframework.data.repository.CrudRepository;

public interface SmallTalkCommentRepository extends CrudRepository<SmallTalkComment, Long> {
	List<SmallTalkComment> findBySmallTalk(SmallTalk smallTalk);

	List<SmallTalkComment> findByWriter(SocialUser writer);
}