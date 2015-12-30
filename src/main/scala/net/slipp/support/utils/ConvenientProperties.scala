package net.slipp.support.utils

import java.io.PrintWriter
import java.io.StringWriter
import java.util.Properties
import java.util.regex.Matcher
import java.util.regex.Pattern
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.util.Assert
import scala.collection.JavaConversions._

/**
  * 편리한 Properties - Properties 클래스를 상속하여 Properties에서 문자열,숫자,boolean 등의 값을 직접 뽑아
  * 낼 수 있으며, 또한, SystemProperties의 경우 ${system.property.name} 형태로 만들어서, 이 값을 실제
  * System Property값으로 변환하여 저장하도록 처리한다.
  */
object ConvenientProperties {
  private var SYSTEM_PROPERTY_NAME_PATTERN: Pattern = Pattern.compile("\\$\\{([\\w\\.]+)\\}")
}

class ConvenientProperties(properties: Properties) extends Properties(properties) {
  private val log: Logger = LoggerFactory.getLogger(classOf[ConvenientProperties])

  Assert.notNull(properties, "properties should not be null")
  processSystemProperties
  logProperties

  private def processSystemProperties {
    for (key <- stringPropertyNames) {
      val value: String = getProperty(key)
      val matcher: Matcher = ConvenientProperties.SYSTEM_PROPERTY_NAME_PATTERN.matcher(value)
      val sb: StringBuffer = new StringBuffer
      while (matcher.find) {
        convertSystemPropertyToValue(matcher, sb)
      }
      matcher.appendTail(sb)
      setProperty(key, sb.toString)
    }
  }

  private def convertSystemPropertyToValue(matcher: Matcher, sb: StringBuffer) {
    val sysPropertyName: String = matcher.group(1)
    val sysPropertyValue: String = System.getProperty(sysPropertyName)
    log.debug(s"sysProperty Name: ${sysPropertyName}, Value: ${sysPropertyValue}")
    if (sysPropertyValue == null) {
      throw new NullPointerException(sysPropertyName + " 이 존재하지 않습니다.")
    }
    matcher.appendReplacement(sb, sysPropertyValue.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$"))
  }

  private def getOptionProperty(key: String) = Option(getProperty(key))

  def getInt(key: String): Int = getInt(key, 0)

  def getInt(key: String, defaultValue: Int) = {
    getOptionProperty(key) match {
      case Some(v) => v.toInt
      case None => defaultValue
    }
  }

  def getLong(key: String): Long = getLong(key, 0L)

  def getLong(key: String, defaultValue: Long) = {
    getOptionProperty(key) match {
      case Some(v) => v.toLong
      case None => defaultValue
    }
  }

  def getDouble(key: String): Double = getDouble(key, 0.0)

  def getDouble(key: String, defaultValue: Double) = {
    getOptionProperty(key) match {
      case Some(v) => v.toDouble
      case None => defaultValue
    }
  }

  def getBoolean(key: String) = getProperty(key).toBoolean

  def logProperties {
    if (log.isDebugEnabled) {
      val sw: StringWriter = new StringWriter
      val pw: PrintWriter = new PrintWriter(sw)
      this.list(pw)
      log.debug("Convinient Properties loaded : {}", sw.toString)
      pw.close
    }
  }
}