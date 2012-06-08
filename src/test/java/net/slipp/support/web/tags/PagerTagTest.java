package net.slipp.support.web.tags;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;

@SuppressWarnings("rawtypes")
@RunWith(MockitoJUnitRunner.class)
public class PagerTagTest {
	@Mock
	private PageContext pageContext;

	@Mock
	private JspWriter jspWriter;
	
	private PagerTag pagerTag = new PagerTag();
	
	@Mock
	private Page page;
	
	@Before
	public void setup() {
		pagerTag.setPage(page);
		
		pagerTag.setJspContext(pageContext);
		when(pageContext.getOut()).thenReturn(jspWriter);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void doTag_totalCount_음수이면안됨() throws JspException, IOException {
		when(page.getTotalElements()).thenReturn(-1L);
		pagerTag.setPage(page);
		pagerTag.doTag();
	}
	
	@Test
	public void generateHtml_totalPage5_currentPage3() throws Exception {
		when(page.getTotalPages()).thenReturn(5);
		when(page.getNumber()).thenReturn(2);
		pagerTag.setPrefixUri("/questions");
		String result = pagerTag.generateHtml();
		
		String expectedHtml = createInActivePage(1) +
				createInActivePage(2) +
				createActivePage(3) +
				createInActivePage(4) +
				createInActivePage(5);
		
		assertThat(result, is(expectedHtml));
	}
	
	@Test
	public void generateHtml_totalPage6_currentPage2() throws Exception {
		when(page.getTotalPages()).thenReturn(6);
		when(page.getNumber()).thenReturn(1);
		pagerTag.setPrefixUri("/questions");
		String result = pagerTag.generateHtml();
		
		String expectedHtml = createInActivePage(1) +
				createActivePage(2) +
				createInActivePage(3) +
				createInActivePage(4) +
				createInActivePage(5) +
				createEmptyPage() +
				createInActivePage(6); 
		
		assertThat(result, is(expectedHtml));
	}
	
	@Test
	public void generateHtml_totalPage6_currentPage4() throws Exception {
		when(page.getTotalPages()).thenReturn(6);
		when(page.getNumber()).thenReturn(3);
		pagerTag.setPrefixUri("/questions");
		String result = pagerTag.generateHtml();
		
		String expectedHtml = createInActivePage(1) +
				createInActivePage(2) +
				createInActivePage(3) +
				createActivePage(4) +
				createInActivePage(5) +
				createEmptyPage() +
				createInActivePage(6); 
		
		assertThat(result, is(expectedHtml));
	}
	
	@Test
	public void generateHtml_totalPage8_currentPage2() throws Exception {
		when(page.getTotalPages()).thenReturn(8);
		when(page.getNumber()).thenReturn(1);
		pagerTag.setPrefixUri("/questions");
		String result = pagerTag.generateHtml();
		
		String expectedHtml = createInActivePage(1) +
				createActivePage(2) +
				createInActivePage(3) +
				createInActivePage(4) + 
				createInActivePage(5) +
				createEmptyPage() +
				createInActivePage(8); 
		assertThat(result, is(expectedHtml));
	}
	
	@Test
	public void generateHtml_totalPage8_currentPage8() throws Exception {
		when(page.getTotalPages()).thenReturn(8);
		when(page.getNumber()).thenReturn(7);
		pagerTag.setPrefixUri("/questions");
		String result = pagerTag.generateHtml();
		
		String expectedHtml = createInActivePage(1) +
				createEmptyPage() +
				createInActivePage(4) +
				createInActivePage(5) +
				createInActivePage(6) +
				createInActivePage(7) + 
				createActivePage(8);
		assertThat(result, is(expectedHtml));
	}
	
	@Test
	public void generateHtml_totalPage8_currentPage5() throws Exception {
		when(page.getTotalPages()).thenReturn(8);
		when(page.getNumber()).thenReturn(4);
		pagerTag.setPrefixUri("/questions");
		String result = pagerTag.generateHtml();
		
		String expectedHtml = createInActivePage(1) +
				createEmptyPage() +
				createInActivePage(4) + 
				createActivePage(5) +
				createInActivePage(6) +
				createInActivePage(7) +
				createInActivePage(8); 
		assertThat(result, is(expectedHtml));
	}
	
	@Test
	public void generateHtml_totalPag11_currentPage7() throws Exception {
		when(page.getTotalPages()).thenReturn(11);
		when(page.getNumber()).thenReturn(6);
		pagerTag.setPrefixUri("/questions");
		String result = pagerTag.generateHtml();
		
		String expectedHtml = createInActivePage(1) +
				createEmptyPage() +
				createInActivePage(5) +
				createInActivePage(6) +
				createActivePage(7) +
				createInActivePage(8) + 
				createInActivePage(9) +
				createEmptyPage() +
				createInActivePage(11); 
		assertThat(result, is(expectedHtml));
	}
	
	private String createInActivePage(int page) {
		return String.format("<li><a href=\"/questions?page=%d\">%d</a></li>", page, page);
	}
	
	private String createActivePage(int page) {
		return String.format("<li class=\"active\"><a href=\"/questions?page=%d\">%d</a></li>", page, page);
	}
	
	private String createEmptyPage() {
		return "<li class=\"disabled\"><a href=\"#\">...</a></li>";
	}
	
	@Test
	public void start_end_totalPage5_currentPage4() throws Exception {
		when(page.getTotalPages()).thenReturn(5);
		when(page.getNumber()).thenReturn(3);
		assertThat(pagerTag.getStart(), is(1));
		assertThat(pagerTag.getEnd(), is(5));
	}
	
	@Test
	public void start_end_totalPage6_currentPage6() throws Exception {
		when(page.getTotalPages()).thenReturn(6);
		when(page.getNumber()).thenReturn(5);
		assertThat(pagerTag.getStart(), is(2));
		assertThat(pagerTag.getEnd(), is(6));
	}
	
	@Test
	public void start_end_totalPage10_currentPage5() throws Exception {
		when(page.getTotalPages()).thenReturn(10);
		when(page.getNumber()).thenReturn(4);
		assertThat(pagerTag.getStart(), is(3));
		assertThat(pagerTag.getEnd(), is(7));
	}
}
