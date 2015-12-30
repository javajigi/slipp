package net.slipp.support.wiki

import java.util.regex.Matcher
import java.util.regex.Pattern
import net.slipp.support.utils.SlippStringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.google.common.collect.ImmutableList
import com.google.common.collect.Lists
import com.google.common.collect.Sets
import scala.collection.JavaConversions._

object SlippWikiUtils {
  private val log: Logger = LoggerFactory.getLogger(classOf[SlippWikiUtils])
  private val IMAGE_WIKI_PATTERN: Pattern = Pattern.compile("!([a-zA-Z0-9]{32})!")

  def createImageListFrom(contents: String) = {
    log.debug("content : {}", contents)
    val matcher: Matcher = IMAGE_WIKI_PATTERN.matcher(contents)
    val images = Lists.newArrayList[String]()

    while (matcher.find) {
        log.debug(s"result : ${matcher.group(1)}")
        if (!images.contains(matcher.group(1))) {
          images.add(matcher.group(1))
        }
    }

    images
  }

  def convertTabToSpace(contents: String): String = {
    if (contents == null) {
      return null
    }
    return contents.replace("\t", "  ")
  }

  def replaceImages(c: String): String = {
    val matcher: Matcher = IMAGE_WIKI_PATTERN.matcher(c)
    val matchString = Sets.newHashSet[String]
    while (matcher.find) {
      log.debug("match : {}", matcher.group(1))
      matchString.add(matcher.group(1))
    }

    var contents = c
    for (each <- matchString) {
      contents = contents.replace("!" + each + "!", createImageHtml(each))
    }
    return contents
  }

  private def createImageHtml(attachmentId: String): String = {
    val imageUrl: String = "/attachments/" + attachmentId
    return "<img src=\"" + imageUrl + "\"/>"
  }

  def convertWiki(c: String) = {
    var contents = c
    contents = contents.replace("h1.", "#")
    contents = contents.replace("h2.", "##")
    contents = contents.replace("h3.", "###")
    contents = contents.replace("h4.", "####")
    contents = contents.replace("h5.", "#####")
    contents = contents.replace("h6.", "######")
    contents = contents.replace("\n\r", "\r\n")
    contents = contents.replace("**", "    *")
    contents = contents.replace("{code}", "```")
    contents = contents.replace("{code:java}", "```")
    contents = contents.replace("{code:bash}", "```")
    contents = contents.replace("{code:xml}", "```")
    SlippStringUtils.convertMarkdownLinks(contents)
  }
}

class SlippWikiUtils
