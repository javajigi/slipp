package net.slipp.service.qna

import java.util.List
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.google.common.collect.Lists

class ListTest {
  private val logger: Logger = LoggerFactory.getLogger(classOf[ListTest])

  @Test
  @throws(classOf[Exception])
  def subList {
    val values: List[String] = Lists.newArrayList("a", "b", "c")
    var subValues: List[String] = values.subList(0, 2)
    logger.debug("subValues : {}", subValues)
    var length: Int = 5
    if (values.size < length) {
      length = values.size
    }
    subValues = values.subList(0, length)
    logger.debug("subValues : {}", subValues)
  }
}
