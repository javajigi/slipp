package net.slipp.service.summary

import net.slipp.domain.summary.SiteSummary
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.assertThat
import org.junit.{Before, Test}
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.runners.MockitoJUnitRunner
import org.slf4j.{Logger, LoggerFactory}

@RunWith(classOf[MockitoJUnitRunner])
class SummaryServiceTest {
  private var logger: Logger = LoggerFactory.getLogger(this.getClass)
  @InjectMocks private var summaryService: SummaryService = new SummaryService

  @Before def setUp {
  }

  @Test
  @throws(classOf[Exception])
  final def testFindOneThumnail_Dzone {
    val siteSummary: SiteSummary = summaryService.findOneThumbnail("http://java.dzone.com/articles/webinar-replay-spring-boot")
    logger.debug("\testFindOneThumnail_Dzone-------------------------------------------------------------------------------------")
    logger.debug(siteSummary.getTitle)
    logger.debug(siteSummary.getThumbnailImage)
    logger.debug(siteSummary.getContents)
    logger.debug("\testFindOneThumnail_Dzone-------------------------------------------------------------------------------------")
  }

  @Test
  @throws(classOf[Exception])
  final def testFindOneThumnail_Image {
    val siteSummary: SiteSummary = summaryService.findOneThumbnail("http://cfile22.uf.tistory.com/image/167615394F33699B201C10")
    logger.debug("\ntestFindOneThumnail_Image-------------------------------------------------------------------------------------")
    logger.debug(siteSummary.getTitle)
    logger.debug(siteSummary.getThumbnailImage)
    logger.debug(siteSummary.getContents)
    logger.debug("\ntestFindOneThumnail_Image-------------------------------------------------------------------------------------")
  }

  @Test
  @throws(classOf[Exception])
  final def testFindOneThumnail_Okjsp {
    val siteSummary: SiteSummary = summaryService.findOneThumbnail("http://www.okjsp.net")
    logger.debug("\ntestFindOneThumnail_Okjsp-------------------------------------------------------------------------------------")
    logger.debug(siteSummary.getTitle)
    logger.debug(siteSummary.getThumbnailImage)
    logger.debug(siteSummary.getContents)
    logger.debug("\ntestFindOneThumnail_Okjsp-------------------------------------------------------------------------------------")
  }

  @Test def testFindOneThumnail_ArcheAge {
    val siteSummary: SiteSummary = summaryService.findOneThumbnail("http://board.archeage.com/community/openboards/290656")
    assertThat(siteSummary.getTitle, is("[기자단]페가수스를 타고 세계 일주를 | 열린게시판 | ArcheAge"))
    logger.debug("\ntestFindOneThumnail_ArcheAge-------------------------------------------------------------------------------------")
    logger.debug(siteSummary.getTitle)
    logger.debug(siteSummary.getThumbnailImage)
    logger.debug(siteSummary.getContents)
    logger.debug("\ntestFindOneThumnail_ArcheAge-------------------------------------------------------------------------------------")
  }

  @Test
  @throws(classOf[Exception])
  final def testFindOneThumnail_Tistory {
    val siteSummary: SiteSummary = summaryService.findOneThumbnail("http://eclipse4j.tistory.com/172")
    assertThat(siteSummary.getTitle, is("0.2%의 짜증과 함께.. 개발은 언제나 즐겁다. | eclipse static import 등록과 Organize Import시 예외 처리."))
    logger.debug("\ntestFindOneThumnail_Tistory-------------------------------------------------------------------------------------")
    logger.debug(siteSummary.getTitle)
    logger.debug(siteSummary.getThumbnailImage)
    logger.debug(siteSummary.getContents)
    logger.debug(siteSummary.getTargetUrl)
    logger.debug("\ntestFindOneThumnail_Tistory-------------------------------------------------------------------------------------")
  }

  @Test
  @throws(classOf[Exception])
  final def testFindOneThumnail_WindtaleNet {
    val siteSummary: SiteSummary = summaryService.findOneThumbnail("http://windtale.net/blog/sublime-text-tip/")
    assertThat(siteSummary.getTitle, is("Sublime Text 유용한 기능과 단축키 - WindTale"))
    logger.debug("\ntestFindOneThumnail_Tistory-------------------------------------------------------------------------------------")
    logger.debug(siteSummary.getTitle)
    logger.debug(siteSummary.getThumbnailImage)
    logger.debug(siteSummary.getContents)
    logger.debug(siteSummary.getTargetUrl)
    logger.debug("\ntestFindOneThumnail_Tistory-------------------------------------------------------------------------------------")
  }
}
