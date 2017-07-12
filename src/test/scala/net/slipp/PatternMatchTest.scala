package net.slipp

import net.slipp.domain.user.SocialUser
import net.slipp.support.test.Fixture
import org.junit.Assert._
import org.junit.Test

class PatternMatchTest extends Fixture {
  private def isEmptyWriterId(writerId: Option[Long]) = {
    writerId match {
      case Some(w) => false
      case _ => true
    }
  }

  @Test def someOption() {
    assertTrue(isEmptyWriterId(None))
    assertFalse(isEmptyWriterId(Some(1L)))

    assertTrue(isEmptyTitle(None))
    assertTrue(isEmptyTitle(Option(null)))
    assertFalse(isEmptyTitle(Option("")))
  }

  private def isEmptyTitle(title: Option[String]) = {
    title match {
      case Some(w) => {
        println(w)
        false
      }
      case _ => true
    }
  }

  @Test def option(): Unit = {
    println(Option(null))
    println(Option("abc"))
    println(Some(null))
    println(Some("abc"))
  }

  @Test def setEmpty() {
    val users = Set[SocialUser]()
    val result = users match {
      case Set => true
      case _ => false
    }
    assertFalse(result)
  }

  @Test def setHasElement() {
    val users = Set[SocialUser](aSomeUser())
    val result = users match {
      case Set => true
      case _ => false
    }
    assertFalse(result)
  }
}
