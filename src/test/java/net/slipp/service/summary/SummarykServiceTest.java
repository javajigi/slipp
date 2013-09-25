package net.slipp.service.summary;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import net.slipp.domain.summary.SiteSummary;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(MockitoJUnitRunner.class)
public class SummarykServiceTest {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@InjectMocks
	private SummaryService summaryService = new SummaryService();

	@Before
	public void setUp() {
	}

	@Test
	@Ignore
	public final void testFindOneThumnail_ArcheAge() throws Exception {
		SiteSummary siteSummary = summaryService
				.findOneThumbnail("http://board.archeage.com/community/openboards/290656");
		logger.debug(siteSummary.getTitle());
		logger.debug(siteSummary.getThumbnailImage());
		logger.debug(siteSummary.getContents());
		assertThat(siteSummary.getTitle(), is("[기자단]페가수스를 타고 세계 일주를 | 열린게시판 | ArcheAge"));
	}
	
	@Test
	public final void testFindOneThumnail_Tistory() throws Exception {
		SiteSummary siteSummary = summaryService
				.findOneThumbnail("http://eclipse4j.tistory.com/172");
		logger.debug(siteSummary.getTitle());
		logger.debug(siteSummary.getThumbnailImage());
		logger.debug(siteSummary.getContents());
		logger.debug(siteSummary.getTargetUrl());
		assertThat(siteSummary.getTitle(), is("0.2%의 짜증과 함께.. 개발은 언제나 즐겁다. | eclipse static import 등록과 Organize Import시 예외 처리."));
	}
}
