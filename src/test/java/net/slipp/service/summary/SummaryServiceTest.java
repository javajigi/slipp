package net.slipp.service.summary;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import net.slipp.domain.summary.SiteSummary;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(MockitoJUnitRunner.class)
public class SummaryServiceTest {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@InjectMocks
	private SummaryService summaryService = new SummaryService();

	@Before
	public void setUp() {
	}

	
	@Test
	public final void testFindOneThumnail_Dzone() throws Exception {
		SiteSummary siteSummary = summaryService
				.findOneThumbnail("http://java.dzone.com/articles/webinar-replay-spring-boot");
		//assertThat(siteSummary.getTitle(), is("SLiPP"));
		logger.debug("\testFindOneThumnail_Dzone-------------------------------------------------------------------------------------");
		logger.debug(siteSummary.getTitle());
		logger.debug(siteSummary.getThumbnailImage());
		logger.debug(siteSummary.getContents());
		logger.debug("\testFindOneThumnail_Dzone-------------------------------------------------------------------------------------");
	}
	
	@Test
	public final void testFindOneThumnail_Image() throws Exception {
		SiteSummary siteSummary = summaryService
				.findOneThumbnail("http://cfile22.uf.tistory.com/image/167615394F33699B201C10");
		//assertThat(siteSummary.getTitle(), is("SLiPP"));
		logger.debug("\ntestFindOneThumnail_Image-------------------------------------------------------------------------------------");
		logger.debug(siteSummary.getTitle());
		logger.debug(siteSummary.getThumbnailImage());
		logger.debug(siteSummary.getContents());
		logger.debug("\ntestFindOneThumnail_Image-------------------------------------------------------------------------------------");
	}
	
	@Test
	public final void testFindOneThumnail_Okjsp() throws Exception {
		SiteSummary siteSummary = summaryService
				.findOneThumbnail("http://www.okjsp.net");
		//assertThat(siteSummary.getTitle(), is("SLiPP"));
		logger.debug("\ntestFindOneThumnail_Okjsp-------------------------------------------------------------------------------------");
		logger.debug(siteSummary.getTitle());
		logger.debug(siteSummary.getThumbnailImage());
		logger.debug(siteSummary.getContents());
		logger.debug("\ntestFindOneThumnail_Okjsp-------------------------------------------------------------------------------------");
	}
	
	@Test
	public final void testFindOneThumnail_SLiPP() throws Exception {
		SiteSummary siteSummary = summaryService
				.findOneThumbnail("http://www.slipp.net");
		assertThat(siteSummary.getTitle(), is("SLiPP"));
		logger.debug("\ntestFindOneThumnail_SLiPP-------------------------------------------------------------------------------------");
		logger.debug(siteSummary.getTitle());
		logger.debug(siteSummary.getThumbnailImage());
		logger.debug(siteSummary.getContents());
		logger.debug("\ntestFindOneThumnail_SLiPP-------------------------------------------------------------------------------------");
	}
	
	@Test
	public final void testFindOneThumnail_ArcheAge() throws Exception {
		SiteSummary siteSummary = summaryService
				.findOneThumbnail("http://board.archeage.com/community/openboards/290656");
		assertThat(siteSummary.getTitle(), is("[기자단]페가수스를 타고 세계 일주를 | 열린게시판 | ArcheAge"));
		logger.debug("\ntestFindOneThumnail_ArcheAge-------------------------------------------------------------------------------------");
		logger.debug(siteSummary.getTitle());
		logger.debug(siteSummary.getThumbnailImage());
		logger.debug(siteSummary.getContents());
		logger.debug("\ntestFindOneThumnail_ArcheAge-------------------------------------------------------------------------------------");

	}

	@Test
	public final void testFindOneThumnail_Tistory() throws Exception {
		SiteSummary siteSummary = summaryService.findOneThumbnail("http://eclipse4j.tistory.com/172");
		assertThat(siteSummary.getTitle(),
				is("0.2%의 짜증과 함께.. 개발은 언제나 즐겁다. | eclipse static import 등록과 Organize Import시 예외 처리."));

		logger.debug("\ntestFindOneThumnail_Tistory-------------------------------------------------------------------------------------");
		logger.debug(siteSummary.getTitle());
		logger.debug(siteSummary.getThumbnailImage());
		logger.debug(siteSummary.getContents());
		logger.debug(siteSummary.getTargetUrl());
		logger.debug("\ntestFindOneThumnail_Tistory-------------------------------------------------------------------------------------");

	}

	@Test
	public final void testFindOneThumnail_WindtaleNet() throws Exception {
		SiteSummary siteSummary = summaryService.findOneThumbnail("http://windtale.net/blog/sublime-text-tip/");
		assertThat(siteSummary.getTitle(), is("Sublime Text 2 유용한 기능과 단축키 정리 - WindTale"));
		logger.debug("\ntestFindOneThumnail_Tistory-------------------------------------------------------------------------------------");
		logger.debug(siteSummary.getTitle());
		logger.debug(siteSummary.getThumbnailImage());
		logger.debug(siteSummary.getContents());
		logger.debug(siteSummary.getTargetUrl());
		logger.debug("\ntestFindOneThumnail_Tistory-------------------------------------------------------------------------------------");

	}
}
