package net.slipp.service.summary

import java.io.IOException
import java.net.MalformedURLException
import java.net.SocketTimeoutException
import java.net.URL
import java.net.URLConnection
import net.slipp.domain.summary.SiteSummary
import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang3.StringUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service class SummaryService {
  private final val CONTENT_TYPE_IMAGE: String = "image"
  private val logger: Logger = LoggerFactory.getLogger(this.getClass)
  private val TIMEOUT: Int = 3 * 1000

  @Cacheable(value = Array("smallTalkCache"), key = "#url") def findOneThumbnail(url: String): SiteSummary = {
    try {
      if (StringUtils.isBlank(url)) {
        return null
      }
      if (isImageDirectURL(url)) {
        return new SiteSummary(FilenameUtils.getName(url), StringUtils.EMPTY, url, url)
      }
      val doc: Document = Jsoup.connect(url).followRedirects(true).timeout(TIMEOUT).get
      return new SiteSummary(doc, url)
    }
    catch {
      case te: SocketTimeoutException => {
        logger.warn(s"URL Connection TimeOut : ${url} - ${te.getMessage}")
      }
      case e: IOException => {
        logger.error(e.getMessage, e)
      }
    }
    return null
  }

  private def isImageDirectURL(url: String): Boolean = {
    val u: URL = new URL(url)
    val urlConnection: URLConnection = u.openConnection
    urlConnection.setConnectTimeout(TIMEOUT)
    Option(urlConnection.getContentType) match {
      case Some(contentType) => contentType.contains((CONTENT_TYPE_IMAGE))
      case None => false
    }
  }

  private def isContentTypeImage(contentType: String): Boolean = {
    return contentType.contains(CONTENT_TYPE_IMAGE)
  }
}