package net.slipp

import org.junit.Assert._
import org.junit.Test

class PatternMatchTest {
  private def isEmptyWriterId(writerId: Option[Long]) = {
    writerId match {
      case Some(w) => false
      case _ => true
    }
  }

  @Test def someOption {
    assertTrue(isEmptyWriterId(None))
    assertFalse(isEmptyWriterId(Some(1L)))
  }
}
