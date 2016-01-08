package net.slipp.support.web.tags

import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Properties
import net.slipp.support.utils.ClasspathResourceUtils
import net.slipp.support.utils.SlippStringUtils
import org.apache.commons.io.IOUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.util.Assert

/**
  * 객체의 URL을 구성한다.
  *
  * xxxF(urlFormat, value) 버전은 urlFormat 으로 String의 Format을 받는다.
  * ${url:imgF("/boards/%s", board.type)} 형태로 사용하며 결과는 /boards/free 와 같은 형태로 나온다.
  */
object UrlTag {
  private val XML_EXTENSION: String = "xml"
  private val STATIC_RESOURCE_URL_PROPERTY_KEY: String = "static.server.urls"
  private val PROPERTIES_RESOURCE_PATH: String = "application-properties.xml"
  private val STATIC_LIB_PROPERTEIS_RESOURCE_PATH: String = "staticlib-properties.xml"
  private val DISABLE_JS_PACK_PROPERTY_KEY: String = "urltag.disable.js.pack"
  protected val APPLICATION_PROPERTIES: Properties = new Properties
  protected val STATIC_LIB_PROPERTIES: Properties = new Properties
  /**
    * js,style 등에 사용할 버전
    */
  var VERSION: String = null
  var STATIC_SERVER_URLS: Array[String] = null

  /**
    * static이 아닌 WAS에서 관리하는 public resource 경로에 대한 prefix
    */
  val RESOURCE_PREFIX: String = "/resources"
  var disableJsPack: Boolean = false

  loadProperties(APPLICATION_PROPERTIES, PROPERTIES_RESOURCE_PATH)
  loadProperties(STATIC_LIB_PROPERTIES, STATIC_LIB_PROPERTEIS_RESOURCE_PATH)
  VERSION = populateVersion
  STATIC_SERVER_URLS = populateStaticServerUrl
  disableJsPack = populateDisableJsPack

  private def loadProperties(properties: Properties, resourcePath: String) {
    var is: InputStream = null
    try {
      is = ClasspathResourceUtils.getResourceAsStream(resourcePath)
      if (resourcePath.toLowerCase.endsWith(XML_EXTENSION)) {
        properties.loadFromXML(is)
      }
      else {
        properties.load(is)
      }
    }
    catch {
      case ex: Exception => {
        throw new RuntimeException(ex.getMessage, ex)
      }
    } finally {
      IOUtils.closeQuietly(is)
    }
  }

  private def populateVersion: String = {
    val sdf: SimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm")
    return sdf.format(new Date)
  }

  private def populateStaticServerUrl: Array[String] = {
    var staticResourceUrlProperty: String = APPLICATION_PROPERTIES.getProperty(STATIC_RESOURCE_URL_PROPERTY_KEY)
    if (staticResourceUrlProperty == null) {
      staticResourceUrlProperty = ""
    }
    return staticResourceUrlProperty.split(",")
  }

  private def populateDisableJsPack = {
    val disableJsPackStr = APPLICATION_PROPERTIES.getProperty(DISABLE_JS_PACK_PROPERTY_KEY, "false")
    disableJsPackStr.toBoolean
  }

  def getProperties: Properties = {
    return APPLICATION_PROPERTIES
  }

  /**
    * static 파일 공통 URL 생성기
    *
    * @param url
	 * 기본 URL
    * @param withVersion
	 * 버전정보 표시 여부
    * @return
    */
  private def staticUrl(url: String, withVersion: Boolean): String = {
    return staticUrl(url, withVersion, null)
  }

  /**
    * static 파일 공통 URL 생성기
    *
    * @param url
	 * 기본 URL
    * @param withVersion
	 * 버전정보 표시 여부
    * @return
    */
  private def staticUrl(url: String, withVersion: Boolean, serverId: Integer): String = {
    Assert.hasText(url, "url을 지정해야 합니다.")
    var sId: Integer = null
    if (serverId == null || serverId.intValue < 0 || serverId.intValue >= STATIC_SERVER_URLS.length) {
      val lastCharExceptExt: Char = getLastCharExceptExt(url)
      sId = lastCharExceptExt % STATIC_SERVER_URLS.length
    }
    var finalUrl: String = STATIC_SERVER_URLS(sId)
    if (withVersion) {
      finalUrl = finalUrl + RESOURCE_PREFIX + "/" + UrlTag.VERSION
    }
    finalUrl += url
    return SlippStringUtils.escapeHtml(finalUrl)
  }

  private def getLastCharExceptExt(url: String): Char = {
    val dotIdx: Int = url.lastIndexOf(".")
    var nameExceptExt: String = url
    if (dotIdx > 0) {
      nameExceptExt = url.substring(0, dotIdx)
    }
    return nameExceptExt.charAt(nameExceptExt.length - 1)
  }

  /**
    * WAS 관리 resource URL 생성기
    *
    * @param url
    * @return
    */
  private def resourceUrl(url: String): String = {
    Assert.hasText(url, "url을 지정해야 합니다.")
    return String.format("%s%s%s", RESOURCE_PREFIX, "", url)
  }

  /**
    * js는 관리툴에서만 사용할 것.
    */
  def js(url: String): String = {
    return staticUrl(url, true)
  }

  def img(url: String): String = {
    return staticUrl(url, true)
  }

  def imgFormat(urlFormat: String, value: String): String = {
    val url: String = String.format(urlFormat, value)
    return img(url)
  }

  def imgFormat(urlFormat: String, value1: String, value2: String): String = {
    val url: String = String.format(urlFormat, value1, value2)
    return img(url)
  }

  def lib(libName: String): String = {
    val url: String = STATIC_LIB_PROPERTIES.getProperty(libName)
    if (StringUtils.isBlank(url)) {
      throw new IllegalArgumentException(libName + "은 존재하지 않는 Static Library 이름입니다.")
    }
    return staticUrl(url, false)
  }

  def resource(url: String): String = {
    return resourceUrl(url)
  }

  def resourceFormat(urlFormat: String, value: String): String = {
    val url: String = String.format(urlFormat, value)
    return resourceUrl(url)
  }

  def resourceFormat(urlFormat: String, value1: String, value2: String): String = {
    val url: String = String.format(urlFormat, value1, value2)
    return resourceUrl(url)
  }

  def style(url: String): String = {
    return staticUrl(url, true)
  }

}

class UrlTag {
  def getVersion: String = {
    return UrlTag.VERSION
  }
}