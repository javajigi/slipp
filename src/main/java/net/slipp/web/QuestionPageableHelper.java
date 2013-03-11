package net.slipp.web;

import net.slipp.domain.qna.Question_;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class QuestionPageableHelper {
	public static Pageable createPageable(Integer currentPage, Integer pageSize) {
		Sort sort = new Sort(Direction.DESC, Question_.updatedDate.getName());
		return new PageRequest(currentPage - 1, pageSize, sort);
	}
}
