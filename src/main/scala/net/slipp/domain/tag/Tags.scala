package net.slipp.domain.tag

import java.util.Set
import scala.collection.JavaConversions._

class Tags(tags: Set[Tag]) {
  def getConnectedGroupTags: Set[Tag] = {
    tags.filter(tag => tag.isPooled && tag.isConnectGroup)
  }
}
