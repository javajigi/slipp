package net.slipp.web;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class QnAPageableHelper {
	public static final int DEFAULT_PAGE_NO = 1;
	
	public static Pageable createPageableByQuestionUpdatedDate(Integer currentPage, Integer pageSize) {
		Sort sort = new Sort(Direction.DESC, "updatedDate");
		return new PageRequest(currentPage - 1, pageSize, sort);
	}
	
	public static Pageable createPageableByAnswerCreatedDate(Integer currentPage, Integer pageSize) {
		Sort sort = new Sort(Direction.DESC, "createdDate");
		return new PageRequest(currentPage - 1, pageSize, sort);
	}
	
	public static Integer revisedPage(Integer page) {
		if (page == null) {
			page = DEFAULT_PAGE_NO;
		}
		return page;
	}
}
