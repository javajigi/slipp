package net.slipp.support.wiki

import org.hamcrest.CoreMatchers.is
import org.junit.Assert._
import org.junit.Test
import org.slf4j.{Logger, LoggerFactory}

class SlippWikiUtilsTest {
  private val logger: Logger = LoggerFactory.getLogger(classOf[SlippWikiUtilsTest])

  @Test
  @throws(classOf[Exception])
  def createImageList {
    val contents: String = TestFileReader.read(this, "images.txt")
    val images = SlippWikiUtils.createImageListFrom(contents)
    assertThat(images.size, is(2))
    logger.debug(images.toString)
  }

  @Test
  @throws(classOf[Exception])
  def replaceImages {
    val contents: String = TestFileReader.read(this, "images.txt")
    val actual: String = SlippWikiUtils.replaceImages(contents)
    logger.debug("result : {}", actual)
  }

  @Test
  @throws(classOf[Exception])
  def convertTabToSpace {
    val contents: String = TestFileReader.read(this, "tab.txt")
    val actual: String = SlippWikiUtils.convertTabToSpace(contents)
    logger.debug("converted contents : {}", actual)
  }

  @Test
  @throws(classOf[Exception])
  def convert {
    val contents: String = TestFileReader.read(this, "text.txt")
    val actual: String = WikiContents.parse(contents)
    logger.debug("convert contents : {}", actual)
  }

  @Test
  @throws(classOf[Exception])
  def convertHeaderTagToSharp {
    assertThat(SlippWikiUtils.convertWiki("h1. 제목"), is("# 제목"))
    assertThat(SlippWikiUtils.convertWiki("h2. 제목"), is("## 제목"))
    assertThat(SlippWikiUtils.convertWiki("h3. 제목"), is("### 제목"))
    assertThat(SlippWikiUtils.convertWiki("h4. 제목"), is("#### 제목"))
    assertThat(SlippWikiUtils.convertWiki("h5. 제목"), is("##### 제목"))
    assertThat(SlippWikiUtils.convertWiki("h6. 제목"), is("###### 제목"))
  }
}
