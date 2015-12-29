package net.slipp.support.wiki.pegdown

import org.hamcrest.CoreMatchers._
import org.junit.Assert._
import org.junit.Before
import org.junit.Test
import org.pegdown.PegDownProcessor
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PegDownProcessorTest {
  private val log: Logger = LoggerFactory.getLogger(classOf[PegDownProcessorTest])
  private var dut: PegDownProcessor = null

  @Before def setup {
    dut = new PegDownProcessor
  }

  @Test
  @throws(classOf[Exception])
  def header {
    var result: String = dut.markdownToHtml("## 제목")
    assertThat(result, is("<h2>제목</h2>"))
    result = dut.markdownToHtml("#### 제목")
    assertThat(result, is("<h4>제목</h4>"))
  }

  @Test
  @throws(classOf[Exception])
  def list {
    var result: String = dut.markdownToHtml("* 리스트1\r* 리스트2")
    log.debug("result: {}", result)
    result = dut.markdownToHtml("- 리스트1")
    log.debug("result: {}", result)
    result = dut.markdownToHtml("* 리스트1\r    * 리스트2")
    log.debug("result: {}", result)
  }

  @Test
  @throws(classOf[Exception])
  def quote {
    val result: String = dut.markdownToHtml("> 인용1\r> 인용2")
    log.debug("result: {}", result)
  }

  @Test
  @throws(classOf[Exception])
  def bold {
    val result: String = dut.markdownToHtml("테스트 **강조** 음냐하.")
    log.debug("result: {}", result)
  }

  @Test
  @throws(classOf[Exception])
  def italic {
    val result: String = dut.markdownToHtml("테스트 *이태리* 음냐하.")
    log.debug("result: {}", result)
  }

  @Test
  @throws(classOf[Exception])
  def code {
    val result: String = dut.markdownToHtml("```\r@Test\rpublic void 시간을돌린다2() throws Exception{```")
    log.debug("result: {}", result)
  }
}
