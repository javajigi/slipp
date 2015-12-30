package net.slipp.support.web.tags

import java.util.Collection

import net.slipp.domain.user.SocialUser
import net.slipp.support.utils.SlippStringUtils
import net.slipp.support.wiki.{SlippWikiUtils, WikiContents}
import org.apache.commons.lang3.StringUtils

/**
  * JSP EL Functions
  */
object SlippFunctions {
  /**
    * 문자열의 HTML을 Escape한다.
    *
    * @param str
    * @return
    */
  def h(str: String): String = {
    return SlippStringUtils.escapeHtml(str)
  }

  /**
    * 문자열의 새줄기호를 &lt;br&gt; 로 변경한다.
    *
    * @param str
    * @return
    */
  def br(str: String): String = {
    return SlippStringUtils.newLineToBr(str)
  }

  /**
    * 문자열의 HTML을 Escape해 주고, 새줄기호를 &lt;br&gt;로 변경한다.
    *
    * @param str
    * @return
    */
  def hbr(str: String): String = {
    return SlippStringUtils.escapeHtmlAndNewLineToBr(str)
  }

  /**
    * 문자열에 있는 링크를 a 태그로 변환한다.
    *
    * @param str
    * @return
    */
  def links(str: String): String = {
    return SlippStringUtils.populateLinks(str)
  }

  /**
    * 문자열에 있는 링크를 a 태그로 변환한다.
    *
    * @param str
    * @param title
    * @return
    */
  def linksToTitle(str: String, title: String): String = {
    return SlippStringUtils.populateLinks(str, title)
  }

  /**
    * 일반 문자열을 범용적인 형태로 화면상에 출력한다. HTML Escape를 수행하고, 새줄 기호를 &lt;br&gt; 태그로 바꾸고,
    * 링크 문자열(http://... 등)에 &lt;a&gt; 태그를 지정해준다.
    *
    * @param str
    * @return
    */
  def plainText(str: String): String = {
    return SlippStringUtils.plainText(str)
  }

  def trimPlainText(str: String): String = {
    return SlippStringUtils.trimPlainText(str)
  }

  /**
    * URL Encoding을 수행한다.
    *
    * @param str
    * @return
    */
  def urlEncode(str: String): String = {
    return SlippStringUtils.urlEncode(str)
  }

  /**
    * 표현식이 true이면 value를 출력하고 아니면 아무것도 출력하지 않는다.
    *
    * @param expression
    * @param value
    * @return
    */
  def whenTrue(expression: Boolean, value: AnyRef): AnyRef = {
    if (expression) {
      return value
    }
    return null
  }

  def whenTrueOr(expression: Boolean, trueValue: AnyRef, falseValue: AnyRef): AnyRef = {
    if (expression) {
      return trueValue
    }
    return falseValue
  }

  /**
    * 표현식이 false이면 value를 출력하고 아니면 아무것도 출력하지 않는다.
    *
    * @param expression
    * @param value
    * @return
    */
  def whenFalse(expression: Boolean, value: AnyRef): AnyRef = {
    return whenTrue(!expression, value)
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
    return SlippStringUtils.stripTagsAndCut(str, maxLength, tail)
  }

  /**
    * 특정 길이 이하의 글자만 남기고 리턴한다.
    *
    * @param str
    * @param maxLength
    * @param tail
    * @return
    */
  def cut(str: String, maxLength: Int, tail: String): String = {
    return SlippStringUtils.cut(str, maxLength, tail)
  }

  /**
    * instanceOf
    *
    * @param o
    * @param className
    * @return
    */
  def instanceOf(o: AnyRef, className: String): Boolean = {
    var returnValue: Boolean = false
    try {
      returnValue = Class.forName(className).isInstance(o)
    }
    catch {
      case e: ClassNotFoundException => {
        returnValue = false
      }
    }
    return returnValue
  }

  @SuppressWarnings(Array("rawtypes")) def hasItem(collection: Collection[_], element: AnyRef): Boolean = {
    if (collection == null || element == null) {
      return false
    }
    return collection.contains(element)
  }

  def isWriter(writer: SocialUser, loginUser: SocialUser): Boolean = {
    if (writer == null) {
      return false
    }
    return writer.isSameUser(loginUser)
  }

  def isWriterOrAdmin(writer: SocialUser, loginUser: SocialUser): Boolean = {
    return isWriter(writer, loginUser) || loginUser.isAdmined
  }

  def wiki(contents: String): String = {
    val result: String = WikiContents.parse(contents)
    return SlippWikiUtils.replaceImages(result)
  }

  def stripHttp(url: String): String = {
    if (StringUtils.isBlank(url)) {
      return ""
    }
    return url.replaceAll("http:", "")
  }

  def removeLink(str: String): String = {
    return StringUtils.defaultIfEmpty(SlippStringUtils.removeLinks(str).trim, "내용이 없어요~")
  }
}
