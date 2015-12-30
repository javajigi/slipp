package net.slipp.domain.summary

import java.io.Serializable
import java.util.Arrays

import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang3.StringUtils
import org.jsoup.nodes.{Document, Element}
import org.jsoup.select.Elements

import scala.beans.BeanProperty
import scala.collection.JavaConversions._

class SiteSummary(t: String, c: String, tImage: String, tUrl: String) extends Serializable {
  @BeanProperty
  var title: String = t

  @BeanProperty
  var contents: String = c

  @BeanProperty
  var thumbnailImage: String = tImage

  @BeanProperty
  var targetUrl: String = tUrl

  def this(doc: Document, targetUrl: String) =
    this(doc.title, SiteContents.getContents(doc), SiteImage.getImage(targetUrl, doc), targetUrl)
}

object SiteImage {
  def getImage(targetUrl: String, doc: Document): String = {
    val imageFromLogo: AbstractImageFromTag = new ImageFromLogo
    val imageFromMetaTag: AbstractImageFromTag = new ImageFromTag(TagType.meta)
    val imageFromLinkTag: AbstractImageFromTag = new ImageFromTag(TagType.link)
    val imageFromImgTag: AbstractImageFromTag = new ImageFromImgTag
    var imageUrl: String = imageFromLogo.getImagePath(targetUrl, doc)
    if (!StringUtils.isBlank(imageUrl)) {
      return imageUrl
    }
    imageUrl = imageFromMetaTag.getImagePath(targetUrl, doc)
    if (!StringUtils.isBlank(imageUrl)) {
      return imageUrl
    }
    imageUrl = imageFromImgTag.getImagePath(targetUrl, doc)
    if (!StringUtils.isBlank(imageUrl)) {
      return imageUrl
    }
    return imageFromLinkTag.getImagePath(targetUrl, doc)
  }
}

object SiteContents {
  def getContents(doc: Document): String = {
    val element: Element = doc.body
    element.getElementsByTag("header").remove
    val elements: Elements = element.getElementsByTag("p")
    import scala.collection.JavaConversions._
    for (contentElement <- elements) {
      val text: String = contentElement.text
      if (text.length > 10) {
        return text
      }
    }
    return null
  }
}

abstract class AbstractImageFromTag {
  def image(doc: Document): String

  def getImagePath(targetUrl: String, doc: Document): String = {
    val imageSrcUrl: String = image(doc)
    if (hasNotImageUrl(imageSrcUrl)) {
      return null
    }
    return getPath(targetUrl, imageSrcUrl, doc)
  }

  private def hasNotImageUrl(imageSrcUrl: String): Boolean = {
    return StringUtils.isBlank(imageSrcUrl)
  }

  private def getPath(targetUrl: String, imageSrcUrl: String, doc: Document): String = {
    if (StringUtils.startsWith(imageSrcUrl, "http") || hasNotImageUrl(imageSrcUrl)) {
      return imageSrcUrl
    }
    if (StringUtils.startsWith(imageSrcUrl, "//")) {
      return "http:" + imageSrcUrl
    }
    if (StringUtils.startsWith(imageSrcUrl, ".")) {
      return targetUrl + FilenameUtils.getName(imageSrcUrl)
    }
    return targetUrl + imageSrcUrl
  }
}

class ImageFromTag extends AbstractImageFromTag {
  private var tagType: TagType = null

  def this(tagType: TagType) {
    this()
    this.tagType = tagType
  }

  def image(doc: Document): String = {
    val element: Element = doc.head
    val imgElements: Elements = element.getElementsByTag(tagType.getTag)
    import scala.collection.JavaConversions._
    for (imgElement <- imgElements) {
      if (hasImage(imgElement)) {
        return imgElement.attr(tagType.getAttrName)
      }
    }
    return null
  }

  private def hasImage(imgElement: Element): Boolean = {
    return tagType.matchResource(imgElement.attr(tagType.getAttrResource))
  }
}

class ImageFromLogo extends AbstractImageFromTag {
  def image(doc: Document): String = {
    return SiteDefaultLogo.findDefaultLogo(doc.baseUri)
  }
}

object ImageFromImgTag {
  val excludes = List("feeds")
}

class ImageFromImgTag extends AbstractImageFromTag {
  def image(doc: Document): String = {
    val element: Element = doc.body
    element.getElementsByTag("header").remove
    val imgs: Elements = element.getElementsByTag("img")
    import scala.collection.JavaConversions._
    for (img <- imgs) {
      if (hasSpecAttribute(img) && hasSize(img)) {
        return img.attr("src")
      }
    }
    return ifEmptyDefaultImage(imgs)
  }

  private def ifEmptyDefaultImage(imgs: Elements): String = {
    for (img <- imgs) {
      for (exclude <- ImageFromImgTag.excludes) {
        if (!StringUtils.contains(img.attr("src"), exclude)) {
          return img.attr("src")
        }
      }
    }
    return null
  }

  private def hasSize(img: Element): Boolean = {
    return img.attr("width").toInt > 50 && img.attr("height").toInt > 50
  }

  private def hasSpecAttribute(img: Element): Boolean = {
    return img.hasAttr("src") && img.hasAttr("width") && img.hasAttr("height")
  }
}