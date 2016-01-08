package net.slipp.support

import java.io.IOException
import java.util.Properties
import net.slipp.support.utils.ClasspathResourceUtils
import net.slipp.support.utils.ConvenientProperties

class SlippEnvironment {
  val properties: Properties = loadProperties
  val convenientProperties: ConvenientProperties = new ConvenientProperties(properties)

  def getProperty(key: String): String = {
    return convenientProperties.getProperty(key)
  }

  private def loadProperties: Properties = {
    val properties: Properties = new Properties
    try {
      properties.loadFromXML(ClasspathResourceUtils.getResourceAsStream("test-application-properties.xml"))
    }
    catch {
      case e: IOException => {
      }
    }
    return properties
  }
}
