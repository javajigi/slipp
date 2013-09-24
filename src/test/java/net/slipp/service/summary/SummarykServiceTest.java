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
public class SummarykServiceTest {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@InjectMocks
	private SummaryService summaryService = new SummaryService();

	@Before
	public void setUp() {
	}

	@Test
	public final void testFindOneThumnail() throws Exception {
		SiteSummary siteSummary = summaryService.findOneThumbnail("http://www.slipp.net");
		logger.debug(siteSummary.getTitle());
		logger.debug(siteSummary.getThumbnailImage());
		assertThat(siteSummary.getTitle(), is("SLiPP"));
	}

}
