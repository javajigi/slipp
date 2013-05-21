package net.slipp.domain.qna;

import static org.springframework.data.jpa.domain.Specifications.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.slipp.domain.user.SocialUser;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

public class QnaSpecifications {
	public static Specification<Question> equalsIsDeleteToQuestion(final boolean isDeleted) {
		return new Specification<Question>() {
			@Override
			public Predicate toPredicate(Root<Question> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get(Question_.deleted), isDeleted);
			}
		};
	}

	private static Specification<Question> equalsWriterIdToQuestion(final SocialUser writer) {
		return new Specification<Question>() {
			@Override
			public Predicate toPredicate(Root<Question> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get(Question_.writer), writer);
			}
		};
	}

	public static Specification<Question> findQuestions(final SocialUser writer, final boolean isDeleted) {
		if (writer == null) {
			return equalsIsDeleteToQuestion(isDeleted);
		}

		Specifications<Question> specs = where(equalsWriterIdToQuestion(writer));
		return specs.and(equalsIsDeleteToQuestion(isDeleted));
	}
	
	public static Specification<Answer> equalsWriterIdToAnswer(final SocialUser writer) {
		return new Specification<Answer>() {
			@Override
			public Predicate toPredicate(Root<Answer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get(Answer_.writer), writer);
			}
		};
	}
}
