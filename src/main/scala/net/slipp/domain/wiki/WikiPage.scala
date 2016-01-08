package net.slipp.domain.wiki

import java.io.Serializable
import java.sql.Timestamp

import scala.beans.BeanProperty

object WikiPage {
  private val DEFAULT_SHORT_CONTENTS_LENGTH: Int = 150
}

class WikiPage(pId: Long, t: String, cDate: Timestamp, c: String) extends Serializable {
  @BeanProperty
  val pageId = pId

  @BeanProperty
  val title = t

  @BeanProperty
  val creationDate = cDate

  @BeanProperty
  val contents = c

  def getShortContents: String = {
    val stripedContents: String = contents.replaceAll("\\<[^>]*>", "")
    if (stripedContents.length < WikiPage.DEFAULT_SHORT_CONTENTS_LENGTH) {
      return stripedContents
    }
    return stripedContents.substring(0, WikiPage.DEFAULT_SHORT_CONTENTS_LENGTH) + "..."
  }
}
