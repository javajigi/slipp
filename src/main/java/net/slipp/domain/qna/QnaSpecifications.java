package net.slipp.domain.qna;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.slipp.domain.qna.Question_;

import org.springframework.data.jpa.domain.Specification;

public class QnaSpecifications {
	public static Specification<Question> equalsIsDelete(final boolean isDeleted) {
		return new Specification<Question>() {
			@Override
			public Predicate toPredicate(Root<Question> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get(Question_.deleted), isDeleted);
			}
		};
	}
}
