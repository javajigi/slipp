package net.slipp.support.web.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;

public class PagerTag extends SimpleTagSupport {
	private static final Logger logger = LoggerFactory.getLogger(PagerTag.class);
	
	private static final int DEFAULT_FIRST_GROUP_SIZE = 5;
	private static final int DEFAULT_PREV_NEXT_SIZE = 2;
	
	@SuppressWarnings("rawtypes")
	private Page page;
	
	private String prefixUri;

	@SuppressWarnings("rawtypes")
	public void setPage(Page page) {
		this.page = page;
	}
	
	public void setPrefixUri(String prefixUri) {
		this.prefixUri = prefixUri;
	}
	
	@Override
	public void doTag() throws JspException, IOException {
		Assert.notNull(page, "page를 지정해야 합니다.");
		Assert.isTrue(page.getTotalElements() >= 0, "totalCount는 0 이상이어야 합니다. 현재 값 : " + page.getTotalElements());
		
		writeHtml(generateHtml());
	}
	
	private void writeHtml(String html) throws IOException {
		JspWriter writer = getJspContext().getOut();
		writer.write(html);
	}
	
	private int getCurrentPage() {
		return page.getNumber() + 1;
	}

	String generateHtml() {
		if (page.getTotalElements() == 0) {
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		
		logger.debug("start : {}, end : {}", getStart(), getEnd());
		
		if (!isFirstGroupByCurrentPage()) {
			sb.append(createPage(1));
			sb.append(createEmptyPage());
		}
		
		for (int i=getStart(); i <= getEnd(); i++) {
			sb.append(createPage(i));
		}
		
		if (!isLastGroup() && !isFirstGroupByTotalPage()) {
			sb.append(createEmptyPage());
			sb.append(createPage(page.getTotalPages()));
		}
		
		return sb.toString();
	}

	private String createPage(int page) {
		if (page == getCurrentPage()) {
			return String.format("<li class=\"active\"><a href=\"%s?page=%d\">%d</a></li>", prefixUri, page, page);
		} else {
			return String.format("<li><a href=\"%s?page=%d\">%d</a></li>", prefixUri, page, page);	
		}
	}
	
	private String createEmptyPage() {
		return "<li class=\"disabled\"><a href=\"#\">...</a></li>";
	}

	int getStart() {
		if (isFirstGroup()) {
			return 1;
		}
		
		if (isLastGroup()) {
			return page.getTotalPages() - DEFAULT_FIRST_GROUP_SIZE + 1;
		}
		
		return getCurrentPage() - DEFAULT_PREV_NEXT_SIZE;
	}

	private boolean isLastGroup() {
		if (isFirstGroup()) {
			return false;
		}
		return page.getTotalPages() - getCurrentPage() + 1 < DEFAULT_FIRST_GROUP_SIZE;
	}

	/**
	 * 현재 페이지가 DEFAULT_FIRST_GROUP_SIZE 보다 크거나 같은 경우 시작 페이지는 <br/>
	 * 현재 페이지에서 DEFAULT_PREV_NEXT_SIZE를 뺀 수가 된다.
	 * @return
	 */
	private boolean isFirstGroup() {
		return getCurrentPage() < DEFAULT_FIRST_GROUP_SIZE;
	}

	int getEnd() {
		if (isFirstGroupByTotalPage()) {
			return page.getTotalPages();
		}
		
		if (isFirstGroupByCurrentPage() ){
			return DEFAULT_FIRST_GROUP_SIZE;
		}
		
		if (isLowerThanNextSize() || isLastGroup()) {
			return page.getTotalPages();
		}
		
		return getCurrentPage() + DEFAULT_PREV_NEXT_SIZE;
	}

	/**
	 * 총 페이지 수가 5보다 크고 현재 페이지가 DEFAULT_FIRST_GROUP_SIZE 보다 작은 경우 DEFAULT_FIRST_GROUP_SIZE를 반환
	 * @return
	 */
	private boolean isFirstGroupByCurrentPage() {
		return getCurrentPage() < DEFAULT_FIRST_GROUP_SIZE;
	}

	/**
	 * 총 페이지 수와 현재 페이지 수 차이가 DEFAULT_PREV_NEXT_SIZE 보다 작거나 같은 경우 총 페이지 수를 반환
	 * @return
	 */
	private boolean isLowerThanNextSize() {
		return page.getTotalPages() - getCurrentPage() <= DEFAULT_PREV_NEXT_SIZE;
	}

	/**
	 * 총 페이지 수가 DEFAULT_FIRST_GROUP_SIZE 보다 작거나 같은 경우 총 페이지 수를 반환
	 * @return
	 */
	private boolean isFirstGroupByTotalPage() {
		return page.getTotalPages() <= DEFAULT_FIRST_GROUP_SIZE;
	}
}
