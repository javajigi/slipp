package net.slipp.support.utils

import java.io.UnsupportedEncodingException
import java.util.regex.Matcher
import java.util.regex.Pattern
import org.apache.commons.lang3.ArrayUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.web.util.UriUtils

object SlippStringUtils {
  val DEFAULT_CHAR_ENCODING: String = "utf-8"
  private val LINK_PATTERN: Pattern = Pattern.compile("(https?|ftp)://[^\\s/$.?#].[^\\s]*")

  def escapeHtml(str: String): String = {
    if (StringUtils.isEmpty(str)) {
      return str
    }
    var escaped: String = str.replaceAll("&", "&amp;")
    escaped = escaped.replaceAll("\\<", "&lt;")
    escaped = escaped.replaceAll("\\>", "&gt;")
    escaped = escaped.replaceAll("\"", "&quot;")
    escaped = escaped.replaceAll("\'", "&#39;")
    escaped = escaped.replaceAll("  ", " &nbsp;")
    return escaped
  }

  def stripTags(str: String): String = {
    if (StringUtils.isEmpty(str)) {
      return ""
    }
    return str.trim.replaceAll("\\<.*?\\>", "")
  }

  def removeLinks(str: String): String = {
    if (StringUtils.isBlank(str)) {
      return str
    }
    val matcher: Matcher = LINK_PATTERN.matcher(str)
    val sb: StringBuffer = new StringBuffer
    while (matcher.find) {
      matcher.appendReplacement(sb, StringUtils.EMPTY)
    }
    matcher.appendTail(sb)
    return sb.toString
  }

  def populateLinks(str: String, maxLength: Int, tail: String, title: String): String = {
    if (StringUtils.isBlank(str)) {
      return str
    }
    val matcher: Matcher = LINK_PATTERN.matcher(str)
    val sb: StringBuffer = new StringBuffer
    while (matcher.find) {
      var link: String = matcher.group
      link = link.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$")
      val t = if (StringUtils.isBlank(title)) cut(link, maxLength, tail) else title
      matcher.appendReplacement(sb, String.format("<a href=\"%s\" target=\"_blank\">%s</a>", link, t))
    }
    matcher.appendTail(sb)
    return sb.toString
  }

  def convertMarkdownLinks(str: String): String = {
    if (StringUtils.isBlank(str)) {
      return str
    }
    val matcher: Matcher = LINK_PATTERN.matcher(str)
    val sb: StringBuffer = new StringBuffer
    while (matcher.find) {
      var link: String = matcher.group
      link = link.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$")
      matcher.appendReplacement(sb, String.format("[%s](%s)", link, link))
    }
    matcher.appendTail(sb)
    return sb.toString
  }

  def populateLinks(str: String): String = {
    return populateLinks(str, -1, null, null)
  }

  def populateLinks(str: String, title: String): String = {
    return populateLinks(str, -1, null, title)
  }

  def cut(str: String, maxLength: Int, tail: String): String = {
    if (StringUtils.isEmpty(str)) {
      return str
    }
    if (maxLength < 0) {
      return str
    }
    if (str.length <= maxLength) {
      return str
    }
    val cutStr: String = str.substring(0, maxLength) + tail
    return cutStr
  }

  def urlEncode(string: String): String = {
    return urlEncode(string, DEFAULT_CHAR_ENCODING)
  }

  def urlEncode(string: String, encoding: String): String = {
    var encoded: String = null
    try {
      encoded = UriUtils.encodeFragment(string, encoding)
    }
    catch {
      case e: UnsupportedEncodingException => {
        throw new IllegalArgumentException(e.getMessage, e)
      }
    }
    return encoded
  }

  def asLongArray(stringArray: Array[String]): Array[Long] = {
    stringArray.map(each => each.toLong)
  }

  /**
    * 문자열의 새줄 기호를 &lt;br&gt; 태그로 변환한다.
    *
    * @param str
    * @return
    */
  def newLineToBr(str: String): String = {
    if (StringUtils.isEmpty(str)) {
      return str
    }
    val converted: String = str.replaceAll("\n", "<br />\n")
    return converted
  }

  /**
    * 문자열의 HTML을 Escape해 주고, 새줄기호를 &lt;br&gt;로 변경한다.
    *
    * @param str
    * @return
    */
  def escapeHtmlAndNewLineToBr(str: String): String = {
    return newLineToBr(escapeHtml(str))
  }

  /**
    * 일반 문자열을 범용적인 형태로 화면상에 출력한다. HTML Escape를 수행하고, 새줄 기호를 &lt;br&gt; 태그로 바꾸고,
    * 링크 문자열(http://... 등)에 &lt;a&gt; 태그를 지정해준다.
    *
    * @param str
    * @return
    */
  def plainText(str: String): String = {
    return newLineToBr(populateLinks(escapeHtml(str)))
  }

  def trimPlainText(str: String): String = {
    return newLineToBr(populateLinks(escapeHtml(StringUtils.trim(str))))
  }

  /**
    * 글의 태그를 모두 제거하고, 특정 길이 이하의 글자만 남기고 리턴한다.
    *
    * @param str
    * @param maxLength
	 * 최대 글 길이
    * @param tail
	 * 글이 잘렸을 경우 꼬리 표시
    * @return
    */
  def stripTagsAndCut(str: String, maxLength: Int, tail: String): String = {
    return cut(stripTags(str), maxLength, tail)
  }

  def merge(p: String, s: String): String = {
    val builder: StringBuilder = new StringBuilder(p)
    builder.append(s)
    return builder.toString
  }

  def getUrlInText(text: String): String = {
    val urlPattern: String = "((https?):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)"
    val p: Pattern = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE)
    val m: Matcher = p.matcher(text)
    if (m.find) {
      return text.substring(m.start(0), m.end(0))
    }
    return null
  }
}

