package net.slipp.repository.qna;

import net.slipp.domain.qna.ScoreLike;
import net.slipp.domain.qna.ScoreLikeType;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ScoreLikeRepository extends CrudRepository<ScoreLike, Long> {

	@Query("select o from ScoreLike o where o.socialUserId = :socialUserId and o.scoreLikeType = :scoreLikeType and o.targetId = :targetId")
	public ScoreLike findBySocialUserIdAnds(@Param("scoreLikeType") ScoreLikeType scoreLikeType,
			@Param("targetId") Long targetId, @Param("socialUserId") Long socialUserId);
}
