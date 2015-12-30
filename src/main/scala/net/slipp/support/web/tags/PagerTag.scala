package net.slipp.support.web.tags

import java.io.IOException
import javax.servlet.jsp.JspException
import javax.servlet.jsp.JspWriter
import javax.servlet.jsp.tagext.SimpleTagSupport
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.util.Assert

object PagerTag {
  private val logger: Logger = LoggerFactory.getLogger(classOf[PagerTag])
  private val DEFAULT_FIRST_GROUP_SIZE: Int = 5
  private val DEFAULT_PREV_NEXT_SIZE: Int = 2
}

class PagerTag extends SimpleTagSupport {
  @SuppressWarnings(Array("rawtypes")) private var page: Page[_] = null
  private var prefixUri: String = null

  @SuppressWarnings(Array("rawtypes")) def setPage(page: Page[_]) {
    this.page = page
  }

  def setPrefixUri(prefixUri: String) {
    this.prefixUri = prefixUri
  }

  override def doTag {
    Assert.notNull(page, "page를 지정해야 합니다.")
    Assert.isTrue(page.getTotalElements >= 0, "totalCount는 0 이상이어야 합니다. 현재 값 : " + page.getTotalElements)
    writeHtml(generateHtml)
  }

  @throws(classOf[IOException])
  private def writeHtml(html: String) {
    val writer: JspWriter = getJspContext.getOut
    writer.write(html)
  }

  private def getCurrentPage: Int = {
    return page.getNumber + 1
  }

  private[tags] def generateHtml: String = {
    if (page.getTotalElements == 0) {
      return ""
    }
    val sb: StringBuilder = new StringBuilder
    PagerTag.logger.debug("start : {}, end : {}", getStart, getEnd)
    if (!isFirstGroupByCurrentPage) {
      sb.append(createPage(1))
      sb.append(createEmptyPage)
    }

    (getStart to getEnd).foreach(i => sb.append(createPage(i)))

    if (!isLastGroup && !isFirstGroupByTotalPage) {
      sb.append(createEmptyPage)
      sb.append(createPage(page.getTotalPages))
    }
    return sb.toString
  }

  private def createPage(page: Int): String = {
    if (page == getCurrentPage) {
      s"""<li class=\"active\"><a href=\"${prefixUri}?page=${page}\">${page}</a></li>"""
    } else {
      s"""<li><a href=\"${prefixUri}?page=${page}\">${page}</a></li>"""
    }
  }

  private def createEmptyPage: String = {
    return "<li class=\"disabled\"><a href=\"#\">...</a></li>"
  }

  private[tags] def getStart: Int = {
    if (isFirstGroup) {
      return 1
    }
    if (isLastGroup) {
      return page.getTotalPages - PagerTag.DEFAULT_FIRST_GROUP_SIZE + 1
    }
    return getCurrentPage - PagerTag.DEFAULT_PREV_NEXT_SIZE
  }

  private def isLastGroup: Boolean = {
    if (isFirstGroup) {
      return false
    }
    return page.getTotalPages - getCurrentPage + 1 < PagerTag.DEFAULT_FIRST_GROUP_SIZE
  }

  /**
    * 현재 페이지가 DEFAULT_FIRST_GROUP_SIZE 보다 크거나 같은 경우 시작 페이지는 <br/>
    * 현재 페이지에서 DEFAULT_PREV_NEXT_SIZE를 뺀 수가 된다.
    * @return
    */
  private def isFirstGroup: Boolean = {
    return getCurrentPage < PagerTag.DEFAULT_FIRST_GROUP_SIZE
  }

  private[tags] def getEnd: Int = {
    if (isFirstGroupByTotalPage) {
      return page.getTotalPages
    }
    if (isFirstGroupByCurrentPage) {
      return PagerTag.DEFAULT_FIRST_GROUP_SIZE
    }
    if (isLowerThanNextSize || isLastGroup) {
      return page.getTotalPages
    }
    return getCurrentPage + PagerTag.DEFAULT_PREV_NEXT_SIZE
  }

  /**
    * 총 페이지 수가 5보다 크고 현재 페이지가 DEFAULT_FIRST_GROUP_SIZE 보다 작은 경우 DEFAULT_FIRST_GROUP_SIZE를 반환
    * @return
    */
  private def isFirstGroupByCurrentPage: Boolean = {
    return getCurrentPage < PagerTag.DEFAULT_FIRST_GROUP_SIZE
  }

  /**
    * 총 페이지 수와 현재 페이지 수 차이가 DEFAULT_PREV_NEXT_SIZE 보다 작거나 같은 경우 총 페이지 수를 반환
    * @return
    */
  private def isLowerThanNextSize: Boolean = {
    return page.getTotalPages - getCurrentPage <= PagerTag.DEFAULT_PREV_NEXT_SIZE
  }

  /**
    * 총 페이지 수가 DEFAULT_FIRST_GROUP_SIZE 보다 작거나 같은 경우 총 페이지 수를 반환
    * @return
    */
  private def isFirstGroupByTotalPage: Boolean = {
    return page.getTotalPages <= PagerTag.DEFAULT_FIRST_GROUP_SIZE
  }
}
