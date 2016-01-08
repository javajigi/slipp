package net.slipp.support.web.tags

import org.hamcrest.CoreMatchers.is
import org.junit.Assert.assertThat
import org.mockito.Mockito.when
import java.io.IOException
import javax.servlet.jsp.JspException
import javax.servlet.jsp.JspWriter
import javax.servlet.jsp.PageContext
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.springframework.data.domain.Page

@SuppressWarnings(Array("rawtypes"))
@RunWith(classOf[MockitoJUnitRunner])
class PagerTagTest {
  @Mock private var pageContext: PageContext = null
  @Mock private var jspWriter: JspWriter = null
  private var pagerTag: PagerTag = new PagerTag
  @Mock private var page: Page[_] = null

  @Before def setup {
    pagerTag.setPage(page)
    pagerTag.setJspContext(pageContext)
    when(pageContext.getOut).thenReturn(jspWriter)
  }

  @Test(expected = classOf[IllegalArgumentException])
  @throws(classOf[JspException])
  @throws(classOf[IOException])
  def doTag_totalCount_음수이면안됨 {
    when(page.getTotalElements).thenReturn(-1L)
    pagerTag.setPage(page)
    pagerTag.doTag
  }

  @Test
  @throws(classOf[Exception])
  def doTag_totalCount_0 {
    when(page.getTotalElements).thenReturn(0L)
    pagerTag.setPage(page)
    pagerTag.doTag
    val result: String = pagerTag.generateHtml
    assertThat(result, is(""))
  }

  @Test
  @throws(classOf[Exception])
  def generateHtml_totalPage1_currentPage1 {
    when(page.getTotalElements).thenReturn(5L)
    when(page.getTotalPages).thenReturn(1)
    when(page.getNumber).thenReturn(0)
    pagerTag.setPrefixUri("/questions")
    val result: String = pagerTag.generateHtml
    val expectedHtml: String = createActivePage(1)
    assertThat(result, is(expectedHtml))
  }

  @Test
  @throws(classOf[Exception])
  def generateHtml_totalPage5_currentPage3 {
    when(page.getTotalElements).thenReturn(30L)
    when(page.getTotalPages).thenReturn(5)
    when(page.getNumber).thenReturn(2)
    pagerTag.setPrefixUri("/questions")
    val result: String = pagerTag.generateHtml
    val expectedHtml: String = createInActivePage(1) + createInActivePage(2) + createActivePage(3) + createInActivePage(4) + createInActivePage(5)
    assertThat(result, is(expectedHtml))
  }

  @Test
  @throws(classOf[Exception])
  def generateHtml_totalPage6_currentPage2 {
    when(page.getTotalElements).thenReturn(30L)
    when(page.getTotalPages).thenReturn(6)
    when(page.getNumber).thenReturn(1)
    pagerTag.setPrefixUri("/questions")
    val result: String = pagerTag.generateHtml
    val expectedHtml: String = createInActivePage(1) + createActivePage(2) + createInActivePage(3) + createInActivePage(4) + createInActivePage(5) + createEmptyPage + createInActivePage(6)
    assertThat(result, is(expectedHtml))
  }

  @Test
  @throws(classOf[Exception])
  def generateHtml_totalPage6_currentPage4 {
    when(page.getTotalElements).thenReturn(30L)
    when(page.getTotalPages).thenReturn(6)
    when(page.getNumber).thenReturn(3)
    pagerTag.setPrefixUri("/questions")
    val result: String = pagerTag.generateHtml
    val expectedHtml: String = createInActivePage(1) + createInActivePage(2) + createInActivePage(3) + createActivePage(4) + createInActivePage(5) + createEmptyPage + createInActivePage(6)
    assertThat(result, is(expectedHtml))
  }

  @Test
  @throws(classOf[Exception])
  def generateHtml_totalPage8_currentPage2 {
    when(page.getTotalElements).thenReturn(30L)
    when(page.getTotalPages).thenReturn(8)
    when(page.getNumber).thenReturn(1)
    pagerTag.setPrefixUri("/questions")
    val result: String = pagerTag.generateHtml
    val expectedHtml: String = createInActivePage(1) + createActivePage(2) + createInActivePage(3) + createInActivePage(4) + createInActivePage(5) + createEmptyPage + createInActivePage(8)
    assertThat(result, is(expectedHtml))
  }

  @Test
  @throws(classOf[Exception])
  def generateHtml_totalPage8_currentPage8 {
    when(page.getTotalElements).thenReturn(30L)
    when(page.getTotalPages).thenReturn(8)
    when(page.getNumber).thenReturn(7)
    pagerTag.setPrefixUri("/questions")
    val result: String = pagerTag.generateHtml
    val expectedHtml: String = createInActivePage(1) + createEmptyPage + createInActivePage(4) + createInActivePage(5) + createInActivePage(6) + createInActivePage(7) + createActivePage(8)
    assertThat(result, is(expectedHtml))
  }

  @Test
  @throws(classOf[Exception])
  def generateHtml_totalPage8_currentPage5 {
    when(page.getTotalElements).thenReturn(30L)
    when(page.getTotalPages).thenReturn(8)
    when(page.getNumber).thenReturn(4)
    pagerTag.setPrefixUri("/questions")
    val result: String = pagerTag.generateHtml
    val expectedHtml: String = createInActivePage(1) + createEmptyPage + createInActivePage(4) + createActivePage(5) + createInActivePage(6) + createInActivePage(7) + createInActivePage(8)
    assertThat(result, is(expectedHtml))
  }

  @Test
  @throws(classOf[Exception])
  def generateHtml_totalPag11_currentPage7 {
    when(page.getTotalElements).thenReturn(30L)
    when(page.getTotalPages).thenReturn(11)
    when(page.getNumber).thenReturn(6)
    pagerTag.setPrefixUri("/questions")
    val result: String = pagerTag.generateHtml
    val expectedHtml: String = createInActivePage(1) + createEmptyPage + createInActivePage(5) + createInActivePage(6) + createActivePage(7) + createInActivePage(8) + createInActivePage(9) + createEmptyPage + createInActivePage(11)
    assertThat(result, is(expectedHtml))
  }

  private def createInActivePage(page: Int): String = {
    s"""<li><a href=\"/questions?page=${page}\">${page}</a></li>"""
  }

  private def createActivePage(page: Int): String = {
    s"""<li class=\"active\"><a href=\"/questions?page=${page}\">${page}</a></li>"""
  }

  private def createEmptyPage: String = {
    return "<li class=\"disabled\"><a href=\"#\">...</a></li>"
  }

  @Test
  @throws(classOf[Exception])
  def start_end_totalPage5_currentPage4 {
    when(page.getTotalElements).thenReturn(30L)
    when(page.getTotalPages).thenReturn(5)
    when(page.getNumber).thenReturn(3)
    assertThat(pagerTag.getStart, is(1))
    assertThat(pagerTag.getEnd, is(5))
  }

  @Test
  @throws(classOf[Exception])
  def start_end_totalPage6_currentPage6 {
    when(page.getTotalPages).thenReturn(6)
    when(page.getNumber).thenReturn(5)
    assertThat(pagerTag.getStart, is(2))
    assertThat(pagerTag.getEnd, is(6))
  }

  @Test
  @throws(classOf[Exception])
  def start_end_totalPage10_currentPage5 {
    when(page.getTotalPages).thenReturn(10)
    when(page.getNumber).thenReturn(4)
    assertThat(pagerTag.getStart, is(3))
    assertThat(pagerTag.getEnd, is(7))
  }
}
